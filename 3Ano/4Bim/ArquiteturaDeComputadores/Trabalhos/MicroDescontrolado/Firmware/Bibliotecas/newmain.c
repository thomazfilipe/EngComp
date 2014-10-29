/**** BIBLIOTECAS *****/
#include <p18f4550.h>
#include "GenericTypeDefs.h"
#include "usb_config.h"
#include "usb_descriptors.c"
#include "USB/usb_device.h"
#include "USB/usb.h"
#include "USB/usb_function_cdc.h"

/**** CONFIGURAÇÕES ****/
#pragma config PLLDIV = 5
#pragma config CPUDIV = OSC1_PLL2
#pragma config USBDIV = 2
#pragma config FOSC = HSPLL_HS
#pragma config BOR = ON
#pragma config BORV = 1 // Se cair mais de 700mili volts ele reseta
#pragma config VREGEN = ON
#pragma config MCLRE = ON

/**** DEFINIÇÕES GLOBAIS ****/
unsigned char buffer[14];
unsigned char c; //Lê um caractere.
void ISR_AltaPrioridade();

/**** INTERRUPÇÕES ****/
#pragma code int_alta = 0x08
void int_alta(){
    _asm GOTO ISR_AltaPrioridade _endasm
}
#pragma code

#pragma interrupt ISR_AltaPrioridade
void ISR_AltaPrioridade(){
    USBDeviceTasks(); // renova a conex. USB em cada interrupção.

    if(USBDeviceState < CONFIGURED_STATE) return;

    getsUSBUSART(&c, 1); // recebe um char OU
    //getsUSBUSART(buffer, 14); // um vetor inteiro

    if(USBUSARTIsTxTrfReady()){
        putsUSBUSART("Hello World!");
    }
}

void main(){

    USBDeviceInit(); // inicializa.
    USBDeviceAttach();  //
    PIR2bits.USBIF = 0;
    IPR2bits.USBIP = 1; // Habilita alta prioridade.

    RCONbits.IPEN = 1; // Interrupções com prioridade.
    INTCONbits.GIEH = 1;
    INTCONbits.GIEL = 0; // Sem interrupt de baixa prioridade.

    while(1);
}