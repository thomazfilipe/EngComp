/* 
 * File:   Aula_I2C.c
 * Author: filipethomaz
 *
 * Created on October 13, 2014, 7:26 PM
 */

#include <p18f4550.h>
#include <i2c.h>

#pragma config MCLR = ON
#pragma WTD = OFF
#pragma FOSC = HS

//#define MASTERI2C
#define BOOL int
#define true 1
#define false 0
#define escrita 0xFE
#define leitura 0x00;

#if MASTERI2C
BOOL escreve_I2C(unsigned char mensagem[], int tamanho, unsigned char endereco){
    IdleI2C();                          // Aguarda pelo Barramento.
    StartI2C();                         // Solicita o início da comunicação.
    while(SSPCON2bits.SEN);             // Aguarda a resolução do Start.
    IdleI2C();

    if(WriteI2C(endereco & escrita))    // Envia endereço e Write
        return false;                   // Retorna como sucesso.

    IdleI2C();
    tamanho--;
    while(tamanho >= 0){
        if(WriteI2C(mensagem[tamanho--]))
            return false;
        IdleI2C();
    }
    StopI2C();
    return true;
}

unsigned char ler_I2C(unsigned char endereco){
    unsigned char dado;

    IdleI2C();
    StartI2C();
    while(SSPCON2bits.SEN);
    IdleI2C();
    if(WriteI2C(endereco | 0x01)) // Define o endereço e Read.
        return false;
    IdleI2C();
    dado = ReadI2C();
    NotAckI2C();
    StopI2C();

    return dado;
}
#else
void ISR_ALTA(void);
void TrataI2C(void);

#pragma code int_alta = 0x08
void int_alta(void){
    _asm GOTO ISR_ALTA _endasm
}
#pragma code

#pragma interrupt ISR_ALTA
void ISR_ALTA(void)
{
    if(PIR1bits.SSPIF)
    {
        TrataI2C();
        PIR1bits.SSPIF = 0;
    }
}

void TrataI2C()
{
    unsigned char aux, dados;

    aux = SSPSTAT & 0x2D;
    if((aux ^ 0x29) == 0x00)
    {
        dados = ReadI2C();
        AckI2C();
    }
}

#endif

void main(void) {
    
#ifdef MASTERI2C
    /* DEFINIÇÕES DE PINOS */
    TRISC = 0x01;       // Define SCL como saída e SDA como entrada.

    /* CONFIGURAÇÕES I2C */
    OpenI2C(MASTER,     // Define como Master
            SLEW_OFF,); // Define como 100KHz ou 1MHz.
    SSPADD = 49;        // (50 - 1).

    while(1);

#else                       // Modo Slave
    /* DEFINIÇÕES DE PINOS */
    TRISC = 0x03;           // Define SCL e SDA como entrada.

    /* CONFIGURAÇÃO DE INTERRUPÇÃO */
    RCONbits.IPEN = 1;
    INTCON |= 0xC0;
    PIE1 |= 0x08;
    IPR1 |= 0x08;

    /* CONFIGURAÇÕES I2C */
    OpenI2C(SLAVE_7,
            SLEW_OFF);

    SSPADD = 0xED;

    while(1);

#endif

}

