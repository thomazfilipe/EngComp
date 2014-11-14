/** D E F I N I Ç Õ E S ******************************************************/
#define self_power          1   // Leitores analógicos
#define USB_BUS_SENSE       1   // Se tiver tensão no USB, trata a interrupção do USB.
#define CLOCK_FREQ 48000000     // Freq. p/ biblioteca.

/** I N C L U D E S **********************************************************/
#include "./USB/usb.h"
#include "./USB/usb_function_cdc.h"
#include "GenericTypeDefs.h"
#include "Compiler.h"
#include "usb_config.h"
#include "USB/usb_device.h"
#include "USB/usb.h"
#include <delays.h>
#include <timers.h>
#include <stdio.h>
#include <p18f4550.h>
#include <adc.h>
#include <string.h>
#include <i2c.h>



/** C O N F I G U R A Ç Õ E S  ***********************************************/
#pragma config PLLDIV   = 5         // (20 MHz crystal on PICDEM FS USB board)
#pragma config CPUDIV   = OSC1_PLL2
#pragma config USBDIV   = 2         // Clock source from 96MHz PLL/2
#pragma config FOSC     = HSPLL_HS  // Gera os 48MHz
#pragma config WDT      = ON        // Watchdog Timer
#pragma config WDTPS    = 512     // Período do WDT em 512ms
#pragma config BOR      = ON
#pragma config BORV     = 3         // Reseta se a tensão for menor que 3v.
#pragma config VREGEN   = ON        //USB Voltage Regulator
#pragma config MCLRE    = ON
#pragma config STVREN   = ON        // requisito da biblioteca.

#pragma config LVP      = OFF       // Desabilita modo de programação de baixa tensão.
#pragma config LPT1OSC  = ON        // Opera em modo de baixo consumo.

#define BOOL int
#define true 1
#define false 0
#define escrita 0xFE
#define leitura 0x00
#define auxTam 10
#define maxTam 74

/** V A R I Á V E I S ********************************************************/
#pragma udata

char aux[74];
unsigned int quant_interrupt = 0;
unsigned char segundo = 0;
int inteiro;
char recebido[74];
char programa[auxTam];
char ultimoPrograma[auxTam];
unsigned char endereco = 0;
unsigned char parametro1[4];
unsigned char parametro2[4];
char resultado[4];
unsigned int resultadoPOS = 0;
unsigned int isMaster = true;
char dado = 0;


/** P R O T Ó T I P O S  D E  F U N Ç Õ E S  *********************************/
//void USBDeviceTasks(void);
void HighPriorityISRCode();
void LowPriorityISRCode();
void USBCBSendResume(void);

//** F U N Ç Õ E S ***********************************************************/
BOOL escreve_I2C(unsigned char mensagem[], int tamanho, unsigned char endereco)
{
    IdleI2C();                          // Aguarda pelo Barramento.
    StartI2C();                         // Solicita o inÌcio da comunicaÁ„o.
    while(SSPCON2bits.SEN);             // Aguarda a resoluÁ„o do Start.
    IdleI2C();

    if(WriteI2C(endereco & escrita))    // Envia endereÁo e Write
        return 0;                       // Retorna como sucesso.

    IdleI2C();
    tamanho--;
    while(tamanho >= 0){
        if(WriteI2C(mensagem[tamanho--]))
            return 0;
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
    if(WriteI2C(endereco | 0x01)) // Define o endereÁo e Read.
        return false;
    IdleI2C();
    dado = ReadI2C();
    NotAckI2C();
    StopI2C();

    return dado;
}


/** I N T E R R U P Ç Õ E S *************************************************/
#define REMAPPED_HIVAddress 0x1008  // End. remapeado p/ interrupt de Alta Prioridade (desl. 1000 pos.).
#define REMAPPED_LIVAddress 0x1018  // para Baixa prioridade.

// Tratamento do Reset
extern void _startup (void);
#pragma code REMAPPED_RESET_VECTOR = 0x1000
void _reset (void)
{
    _asm goto _startup _endasm
}

// Desvios do ISR
#pragma code REMAPPED_HIGH_INTERRUPT_VECTOR = REMAPPED_HIVAddress
void Remapped_High_ISR (void)
{
    _asm goto HighPriorityISRCode _endasm
}
#pragma code REMAPPED_LOW_INTERRUPT_VECTOR = REMAPPED_LIVAddress
void Remapped_Low_ISR (void)
{
    _asm goto LowPriorityISRCode _endasm
}

#pragma code HIGH_INTERRUPT_VECTOR = 0x08
void High_ISR (void)
{
    _asm goto REMAPPED_HIVAddress _endasm
}
#pragma code LOW_INTERRUPT_VECTOR = 0x18
void Low_ISR (void)
{
    _asm goto REMAPPED_LIVAddress _endasm
}
#pragma code


//These are your actual interrupt handling routines.
#pragma interrupt HighPriorityISRCode
void HighPriorityISRCode()
{
    USBDeviceTasks(); // Obrigatório! Qual a com. ativa com o computador.
    if(PIR1bits.SSPIF)  // Se tiver uma iterrupção I2C...
    {
        unsigned char aux;
        aux = SSPSTAT & 0x2D;
        if((aux ^ 0x29) == 0x00) // Se for leitura...
        {
            if(resultadoPOS > 3) resultadoPOS = 0;
            resultado[resultadoPOS] = ReadI2C();
            AckI2C();
            resultadoPOS++;
        }
        PIR1bits.SSPIF = 0;     // Zera o Flag de interrupção I2C
        isMaster = true;        // Finaliza voltando a ser MASTER
    }
}
#pragma interruptlow LowPriorityISRCode
void LowPriorityISRCode()
{
    quant_interrupt++;
    if(quant_interrupt == 201){
        segundo++;
        LATD = LATD ^ (1<<3);   // Muda o estado do LED 22.
        if(segundo == 30) segundo = 0;
        quant_interrupt = 0;
    }
    PIR1bits.TMR2IF = 0;
}
#pragma code


void main(void)
{

    TRISAbits.TRISA5 = 0;       // define saida na porta fisica 7 - LED
    LATAbits.LATA5   = 1;       // Define nível
    TRISCbits.TRISC0 = 0;       // define saida na porta fisica 14 - LED
    LATCbits.LATC0   = 1;       // Define nível
    TRISDbits.TRISD2 = 0;       // Define saida na porta fisica 21 - LED PLACA
    LATDbits.LATD2   = 1;       // Define nível
    TRISDbits.TRISD3 = 0;       // Define saida na porta fisica 22 - LED PLACA
    LATDbits.LATD3   = 0;       // Define nível lógico 0

    TRISAbits.TRISA0 = 0;       // Define porta A0 como saída.
    TRISAbits.TRISA1 = 0;       // Define porta A1 como saída.
    TRISAbits.TRISA2 = 0;       // Define porta A2 como saída.
    TRISAbits.TRISA3 = 0;       // Define porta A3 como saída.
    LATAbits.LATA0   = 0;       // Define nível lógico 0.
    LATAbits.LATA1   = 0;       // Define nível lógico 0.
    LATAbits.LATA2   = 0;       // Define nível lógico 0.
    LATAbits.LATA3   = 0;       // Define nível lógico 0.

    ADCON1 |= 0x0F;             // Default all pins to digital

    USBDeviceInit();

    // TIMER2
    PR2 = 249;

    OpenTimer2(TIMER_INT_OFF    // Desabilita a interrupção TIMER2.
               &T2_POST_1_15    // Postscaler igual a 10 (1:10).
               &T2_PS_1_16);    // Prescaler igual a 16 (1:16).

    PIR1bits.TMR2IF = 0;        // Limpa o flag bit da interrupção TIMER2.
    IPR1bits.TMR2IP = 0;        // Seleciona baixa prioridade.
    PIE1bits.TMR2IE = 1;        // Habilita a interrupção de TIMER2.

    RCONbits.IPEN   = 1;        // Habilita interrupção com prioridade: (0=Alto e 1=baixo)
    INTCONbits.GIEH = 1;        // Habilita todas as interrupções de alta prioridade.
    INTCONbits.GIEL = 1;        // Habilita todas as interrupções de baixa prioridade.

    WriteTimer2(0);             //Limpa o registro.

    // Configura todas as portas multiplexadas com o módulo conversor A/D, como I/O Digital.
    OpenADC(0x00, 0x00, ADC_0ANA);
    // Desabilita o conversor A/D e a interrupção associada a ele.
    CloseADC();

    //-- Interrupt 1 - pino 34
//    TRISBbits.RB1 = 1;          // Define o pino RB2 como entrada.
//
//    INTCON2bits.INTEDG1 = 0;    // Interrupção na borda de subida.
//    INTCON3bits.INT1IF  = 0;    // Limpa o flag bit da interrupção INT1.
//    INTCON3bits.INT1IP  = 1;    // Alta prioridade.
//    INTCON3bits.INT1IE  = 1;    // Ativa a interrupção externa INT1 (RB1).

    //-- Interrupt 2 - pino 35
    TRISBbits.RB2 = 1;          // Define o pino RB2 como entrada.

    INTCON2bits.INTEDG2 = 0;    // Interrupção na borda de subida.
    INTCON3bits.INT2IF  = 0;    // Limpa o flag bit da interrupção INT2.
    INTCON3bits.INT2IP  = 1;    // Alta prioridade.
    INTCON3bits.INT2IE  = 1;    // Ativa a interrupção externa INT2 (RB2).

    //-- Interrupt Geral
    INTCON2bits.RBPU = 1;       // All PORTB pullups disabled
    RCONbits.IPEN = 1;          // Habilita interrupção com nível de prioridade.
    INTCONbits.GIEH = 1;        // Habilita todas as interrupções de alta prioridade
    INTCONbits.GIEL = 1;        // Habilita todas as interrupções de baixa prioridade

    while(1)
    {
        //-- MODO MASTER -------------------------------------------------------
//        LATD = LATDbits.LATD2 = 1; // Liga o LED 21.
        LATD = LATD |= (1<<2);  // Liga o LED 21.

        // CONFIG I2C
//        TRISC = 0x01;           // Define SCL como saída e SDA como entrada.
        TRISCbits.TRISC0 = 1;   // Define SCL como saída   - pino 34
        TRISCbits.TRISC1 = 0;   // Define SDA como entrada - pino 33
        OpenI2C(MASTER,         // Define como Master
                SLEW_OFF);      // Define como 100KHz ou 1MHz.
        SSPADD = 49;            // (50 - 1). BaudRate?
//        RCONbits.IPEN = 0;      // Interrupção I2C Desativada.
        SSPCON2bits.GCEN = 0;   // Desativa mensagens de broadcast
        

        while(isMaster)
        {
            ClrWdt(); // Reseta contador do WatchDogTimer

            if(USB_BUS_SENSE && (USBGetDeviceState() == DETACHED_STATE)) // Se estiver desconectado...
                USBDeviceAttach(); // Conecta.


            // ENVIAR PELA USB
            if(!((USBDeviceState < CONFIGURED_STATE)||(USBSuspendControl==1)))
            {
                if(USBUSARTIsTxTrfReady())                          // Verifica se o módulo pode transmitir.
                {
                    memset(recebido, '\0', maxTam);                     // Reseta string recebido.
                    memset(programa, '\0', auxTam);
                    inteiro = getsUSBUSART(recebido, auxTam);       // Recebe o comando por USB.
                    
                    if(inteiro != 0){                               // Se tiver novo dado, avisa no console.
                        inteiro = 0;
                        CDCTxService();
                        strncpy(programa, recebido, 9);                 // Copia somente 9 posições do recebido USB.
                        memset(ultimoPrograma, '\0', auxTam);

                        memset(aux, '\0', maxTam);
                        sprintf(aux, "#Recebido: %s\n", programa);
                        putUSBUSART(aux, strlen(aux));                  // Escreve na USB
                        strncpy(ultimoPrograma, programa, 9);           // Faz uma cópia do programa em execução.
                        memset(programa, '\0', auxTam);

                        if(ultimoPrograma[0] == 'C'){                         // Teste I2C
                            memset(aux, '\0', maxTam);
                            sprintf(aux, "#Write to address: %c\n", ultimoPrograma[1]);
                            putUSBUSART(aux, strlen(aux));              // Escreve na USB
                        }

                        if(ultimoPrograma[0] == 'D'){                         // Teste I2C
                            memset(aux, '\0', maxTam);
                            sprintf(aux, "#Read from address: %c\n", ultimoPrograma[1]);
                            putUSBUSART(aux, strlen(aux));              // Escreve na USB
                        }

                        if(ultimoPrograma[0] == 'A'){                   // Se tiver algum cálculo para fazer...

                            endereco      = ultimoPrograma[0];                // Separa o primeiro char em byte de endereço

                            parametro1[0] = ultimoPrograma[1];                // Separa o primeiro byte do parametro1
                            parametro1[1] = ultimoPrograma[2];                // Separa o segundo byte do parametro1
                            parametro1[2] = ultimoPrograma[3];                // Separa o terceiro byte do parametro1
                            parametro1[3] = ultimoPrograma[4];                // Separa o quarto byte do parametro1

                            parametro2[0] = ultimoPrograma[5];                // Separa o primeiro byte do parametro2
                            parametro2[1] = ultimoPrograma[6];                // Separa o segundo byte do parametro2
                            parametro2[2] = ultimoPrograma[7];                // Separa o terceiro byte do parametro2
                            parametro2[3] = ultimoPrograma[8];                // Separa o quarto byte do parametro2

                            CDCTxService();
                            memset(aux, '\0', auxTam);
                            sprintf(aux, "#Endereço: %c\n", endereco);
                            putUSBUSART(aux, strlen(aux));              // Escreve na USB

                            CDCTxService();
                            memset(aux, '\0', auxTam);
                            sprintf(aux, "#Enviando param1: %c%c%c%c\n", parametro1[0], parametro1[1], parametro1[2], parametro1[3]);
                            putUSBUSART(aux, strlen(aux));              // Escreve na USB
//                            escreve_I2C(parametro1, 4, endereco);       // Envia o primeiro parâmetro
//                            memset(parametro1, '\0', 4);
//
                            CDCTxService();
                            memset(aux, '\0', auxTam);
                            sprintf(aux, "#Enviando param2: %c%c%c%c\n", parametro2[0], parametro2[1], parametro2[2], parametro2[3]);
                            putUSBUSART(aux, strlen(aux));              // Escreve na USB
//                            escreve_I2C(parametro2, 4, endereco);       // Envia o primeiro byte
//                            memset(parametro2, '\0', 4);

                            isMaster = false;                           // Vira SLAVE e aguarda o resultado.
                        }
                    }
                }
                CDCTxService(); //
            }
            Delay10KTCYx(50);
        }

        CDCTxService();
        memset(aux, '\0', auxTam);
        sprintf(aux, "#MODO SLAVE!\n");
        putUSBUSART(aux, strlen(aux));              // Escreve na USB

        //-- MODO SLAVE --------------------------------------------------------
//        LATD = LATDbits.LATD2 = 0;  // Desliga o LED 21.
        LATD = LATD &= ~(1<<2);  // Desliga o LED 21.

        // CONFIG I2C SLAVE
//        TRISC = 0x03;           // Define SCL e SDA como entrada.
        TRISCbits.TRISC0 = 1;   // Define SCL como entrada - pino 34
        TRISCbits.TRISC1 = 1;   // Define SDA como entrada - pino 33
        RCONbits.IPEN = 1;      // Habilita Interrupções I2C
        SSPCON2bits.GCEN = 0;   // Desativa mensagens de broadcast
        INTCON |= 0xC0;
        PIE1 |= 0x08;
        IPR1 |= 0x08;

        OpenI2C(SLAVE_7,
                SLEW_OFF);
        SSPADD = 0xFF;          // Endereço I2C

        ClrWdt();               // Reseta contador WTD
        while(!isMaster)        // Aguarda interrupção I2C ou reset do WTD
        {
            CDCTxService();
            Delay10KTCYx(500);
        }

    }
}


// ******************************************************************************************************
// ************** USB Callback Functions ****************************************************************
// ******************************************************************************************************
// The USB firmware stack will call the callback functions USBCBxxx() in response to certain USB related
// events.  For example, if the host PC is powering down, it will stop sending out Start of Frame (SOF)
// packets to your device.  In response to this, all USB devices are supposed to decrease their power
// consumption from the USB Vbus to <2.5mA each.  The USB module detects this condition (which according
// to the USB specifications is 3+ms of no bus activity/SOF packets) and then calls the USBCBSuspend()
// function.  You should modify these callback functions to take appropriate actions for each of these
// conditions.  For example, in the USBCBSuspend(), you may wish to add code that will decrease power
// consumption from Vbus to <2.5mA (such as by clock switching, turning off LEDs, putting the
// microcontroller to sleep, etc.).  Then, in the USBCBWakeFromSuspend() function, you may then wish to
// add code that undoes the power saving things done in the USBCBSuspend() function.

// The USBCBSendResume() function is special, in that the USB stack will not automatically call this
// function.  This function is meant to be called from the application firmware instead.  See the
// additional comments near the function.

/******************************************************************************
 * Function:        void USBCBSuspend(void)
 * Overview:        Call back that is invoked when a USB suspend is detected
 *****************************************************************************/
void USBCBSuspend(void)
{
    //IMPORTANT NOTE: Do not clear the USBActivityIF (ACTVIF) bit here.  This bit is
	//cleared inside the usb_device.c file.  Clearing USBActivityIF here will cause
	//things to not work as intended.
}

/******************************************************************************
 * Function:        void USBCBWakeFromSuspend(void)

 *****************************************************************************/
void USBCBWakeFromSuspend(void)
{

}


/*******************************************************************
 * Function:        void USBCBCheckOtherReq(void)
 *
 * PreCondition:    None
 *
 * Input:           None
 *
 * Output:          None
 *
 * Side Effects:    None
 *
 * Overview:        When SETUP packets arrive from the host, some
 * 					firmware must process the request and respond
 *					appropriately to fulfill the request.  Some of
 *					the SETUP packets will be for standard
 *					USB "chapter 9" (as in, fulfilling chapter 9 of
 *					the official USB specifications) requests, while
 *					others may be specific to the USB device class
 *					that is being implemented.  For example, a HID
 *					class device needs to be able to respond to
 *					"GET REPORT" type of requests.  This
 *					is not a standard USB chapter 9 request, and
 *					therefore not handled by usb_device.c.  Instead
 *					this request should be handled by class specific
 *					firmware, such as that contained in usb_function_hid.c.
 *
 * Note:            None
 *******************************************************************/
void USBCBCheckOtherReq(void)
{
    USBCheckCDCRequest();
}//end


/*******************************************************************
 * Function:        void USBCBInitEP(void)
 *
 * PreCondition:    None
 *
 * Input:           None
 *
 * Output:          None
 *
 * Side Effects:    None
 *
 * Overview:        This function is called when the device becomes
 *                  initialized, which occurs after the host sends a
 * 					SET_CONFIGURATION (wValue not = 0) request.  This
 *					callback function should initialize the endpoints
 *					for the device's usage according to the current
 *					configuration.
 *
 * Note:            None
 *******************************************************************/
void USBCBInitEP(void)
{
    CDCInitEP();
}

/********************************************************************
 * Function:        void USBCBSendResume(void)
 *
 * PreCondition:    None
 *
 * Input:           None
 *
 * Output:          None
 *
 * Side Effects:    None
 *
 * Overview:        The USB specifications allow some types of USB
 * 					peripheral devices to wake up a host PC (such
 *					as if it is in a low power suspend to RAM state).
 *					This can be a very useful feature in some
 *					USB applications, such as an Infrared remote
 *					control	receiver.  If a user presses the "power"
 *					button on a remote control, it is nice that the
 *					IR receiver can detect this signalling, and then
 *					send a USB "command" to the PC to wake up.
 *
 *					The USBCBSendResume() "callback" function is used
 *					to send this special USB signalling which wakes
 *					up the PC.  This function may be called by
 *					application firmware to wake up the PC.  This
 *					function will only be able to wake up the host if
 *                  all of the below are true:
 *
 *					1.  The USB driver used on the host PC supports
 *						the remote wakeup capability.
 *					2.  The USB configuration descriptor indicates
 *						the device is remote wakeup capable in the
 *						bmAttributes field.
 *					3.  The USB host PC is currently sleeping,
 *						and has previously sent your device a SET
 *						FEATURE setup packet which "armed" the
 *						remote wakeup capability.
 *
 *                  If the host has not armed the device to perform remote wakeup,
 *                  then this function will return without actually performing a
 *                  remote wakeup sequence.  This is the required behavior,
 *                  as a USB device that has not been armed to perform remote
 *                  wakeup must not drive remote wakeup signalling onto the bus;
 *                  doing so will cause USB compliance testing failure.
 *
 *					This callback should send a RESUME signal that
 *                  has the period of 1-15ms.
 *
 * Note:            This function does nothing and returns quickly, if the USB
 *                  bus and host are not in a suspended condition, or are
 *                  otherwise not in a remote wakeup ready state.  Therefore, it
 *                  is safe to optionally call this function regularly, ex:
 *                  anytime application stimulus occurs, as the function will
 *                  have no effect, until the bus really is in a state ready
 *                  to accept remote wakeup.
 *
 *                  When this function executes, it may perform clock switching,
 *                  depending upon the application specific code in
 *                  USBCBWakeFromSuspend().  This is needed, since the USB
 *                  bus will no longer be suspended by the time this function
 *                  returns.  Therefore, the USB module will need to be ready
 *                  to receive traffic from the host.
 *
 *                  The modifiable section in this routine may be changed
 *                  to meet the application needs. Current implementation
 *                  temporary blocks other functions from executing for a
 *                  period of ~3-15 ms depending on the core frequency.
 *
 *                  According to USB 2.0 specification section 7.1.7.7,
 *                  "The remote wakeup device must hold the resume signaling
 *                  for at least 1 ms but for no more than 15 ms."
 *                  The idea here is to use a delay counter loop, using a
 *                  common value that would work over a wide range of core
 *                  frequencies.
 *                  That value selected is 1800. See table below:
 *                  ==========================================================
 *                  Core Freq(MHz)      MIP         RESUME Signal Period (ms)
 *                  ==========================================================
 *                      48              12          1.05
 *                       4              1           12.6
 *                  ==========================================================
 *                  * These timing could be incorrect when using code
 *                    optimization or extended instruction mode,
 *                    or when having other interrupts enabled.
 *                    Make sure to verify using the MPLAB SIM's Stopwatch
 *                    and verify the actual signal on an oscilloscope.
 *******************************************************************/
void USBCBSendResume(void)
{
    static WORD delay_count;

    //First verify that the host has armed us to perform remote wakeup.
    //It does this by sending a SET_FEATURE request to enable remote wakeup,
    //usually just before the host goes to standby mode (note: it will only
    //send this SET_FEATURE request if the configuration descriptor declares
    //the device as remote wakeup capable, AND, if the feature is enabled
    //on the host (ex: on Windows based hosts, in the device manager
    //properties page for the USB device, power management tab, the
    //"Allow this device to bring the computer out of standby." checkbox
    //should be checked).
    if(USBGetRemoteWakeupStatus() == TRUE)
    {
        //Verify that the USB bus is in fact suspended, before we send
        //remote wakeup signalling.
        if(USBIsBusSuspended() == TRUE)
        {
            USBMaskInterrupts();

            //Clock switch to settings consistent with normal USB operation.
            USBCBWakeFromSuspend();
            USBSuspendControl = 0;
            USBBusIsSuspended = FALSE;  //So we don't execute this code again,
            //until a new suspend condition is detected.

            //Section 7.1.7.7 of the USB 2.0 specifications indicates a USB
            //device must continuously see 5ms+ of idle on the bus, before it sends
            //remote wakeup signalling.  One way to be certain that this parameter
            //gets met, is to add a 2ms+ blocking delay here (2ms plus at
            //least 3ms from bus idle to USBIsBusSuspended() == TRUE, yeilds
            //5ms+ total delay since start of idle).
            delay_count = 3600U;
            do
            {
                delay_count--;
            }while(delay_count);

            //Now drive the resume K-state signalling onto the USB bus.
            USBResumeControl = 1;       // Start RESUME signaling
            delay_count = 1800U;        // Set RESUME line for 1-13 ms
            do
            {
                delay_count--;
            }while(delay_count);
            USBResumeControl = 0;       //Finished driving resume signalling

            USBUnmaskInterrupts();
        }
    }
}


/*******************************************************************
 * Function:        void USBCBEP0DataReceived(void)
 *
 * PreCondition:    ENABLE_EP0_DATA_RECEIVED_CALLBACK must be
 *                  defined already (in usb_config.h)
 *
 * Input:           None
 *
 * Output:          None
 *
 * Side Effects:    None
 *
 * Overview:        This function is called whenever a EP0 data
 *                  packet is received.  This gives the user (and
 *                  thus the various class examples a way to get
 *                  data that is received via the control endpoint.
 *                  This function needs to be used in conjunction
 *                  with the USBCBCheckOtherReq() function since
 *                  the USBCBCheckOtherReq() function is the apps
 *                  method for getting the initial control transfer
 *                  before the data arrives.
 *
 * Note:            None
 *******************************************************************/
#if defined(ENABLE_EP0_DATA_RECEIVED_CALLBACK)
void USBCBEP0DataReceived(void)
{
}
#endif

/*******************************************************************
 * Function:        BOOL USER_USB_CALLBACK_EVENT_HANDLER(
 *                        USB_EVENT event, void *pdata, WORD size)
 *
 * PreCondition:    None
 *
 * Input:           USB_EVENT event - the type of event
 *                  void *pdata - pointer to the event data
 *                  WORD size - size of the event data
 *
 * Output:          None
 *
 * Side Effects:    None
 *
 * Overview:        This function is called from the USB stack to
 *                  notify a user application that a USB event
 *                  occured.  This callback is in interrupt context
 *                  when the USB_INTERRUPT option is selected.
 *
 * Note:            None
 *******************************************************************/
BOOL USER_USB_CALLBACK_EVENT_HANDLER(USB_EVENT event, void *pdata, WORD size)
{
    switch(event)
    {
        case EVENT_TRANSFER:
            //Add application specific callback task or callback function here if desired.
            break;
        case EVENT_SOF:
            //USBCB_SOF_Handler();
            break;
        case EVENT_SUSPEND:
            USBCBSuspend();
            break;
        case EVENT_RESUME:
            USBCBWakeFromSuspend();
            break;
        case EVENT_CONFIGURED:
            USBCBInitEP();
            break;
        case EVENT_SET_DESCRIPTOR:
            //USBCBStdSetDscHandler();
            break;
        case EVENT_EP0_REQUEST:
            USBCBCheckOtherReq();
            break;
        case EVENT_BUS_ERROR:
            //USBCBErrorHandler();
            break;
        case EVENT_TRANSFER_TERMINATED:
            //Add application specific callback task or callback function here if desired.
            //The EVENT_TRANSFER_TERMINATED event occurs when the host performs a CLEAR
            //FEATURE (endpoint halt) request on an application endpoint which was
            //previously armed (UOWN was = 1).  Here would be a good place to:
            //1.  Determine which endpoint the transaction that just got terminated was
            //      on, by checking the handle value in the *pdata.
            //2.  Re-arm the endpoint if desired (typically would be the case for OUT
            //      endpoints).
            break;
        default:
            break;
    }
    return TRUE;
}


/** EOF main.c *************************************************/

/* TEMP CODES


LATD = LATD ^ (1<<2); // Muda o estado do LED 21.
putUSBUSART(aux, strlen(aux));  // Escreve na USB

if(inteiro != 0) // Se tem algo para enviar e tiver algum programa.
                {
                    CDCTxService();
                    putUSBUSART(resultado, strlen(resultado));  // Escreve na USB
                    memset(resultado, 0, strlen(resultado));    // Reseta valor de resultado
                }

 *
 * escreve_I2C(programa[1], 1, programa[0]);

                        dado = ler_I2C(programa[0]);
                        memset(aux, '\0', auxTam);
                        sprintf(aux, "#ler I2C: %c\n", programa[1]);
                        putUSBUSART(dado, strlen(dado));              // Escreve na USB


 */