#include <p18f4550>

#pragma config FOSC = HS
#pragma config WDT = OFF
#pragma config MCLRE = ON
#pragma config PWRT = ON
#pragma config PBADEN = OFF // garante que tudo será pino digita (Desabilita conversor AD)
#pragma config CCP2MX = OFF

void main(){
	/* Configurar PR2 com o período de oscilação */
	PR2 = 77;
    
    /* Configuração do Duty Cicle */
    CCPR1L = 25 >> 2;
    CCP1CON |= (25 & 0x03) << 4;
    
    /* Limpar TRISC */
    TRISC &= 0xF0;
    
    /* Configurar PRESCALE e ligar o timer2 */
    T2CON |= 0x07;
    
    /* Configurar o módulo CCP para PWM */
    CCP1CON |= 0x0C;
    
    while (1);
    
    
}
