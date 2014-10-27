//
//  main.c
//  CompiladorBMP
//
//  Created by Filipe  Thomaz on 10/25/14.
//  Copyright (c) 2014 Filipe  Thomaz. All rights reserved.
//

#include <stdio.h>
#include <stdlib.h>
#include <curses.h>
#include <string.h>
#include <termios.h>
#include <unistd.h>
#include <ncurses.h>
#include <math.h>

unsigned char byte0;
unsigned char byte1;
unsigned char byte2;
unsigned char byte3;
int debug = 0;
int delay = 0;

void separaEmBytes(int valor){
 
    byte0 = (valor & 255);
    byte1 = ((valor >> 8) & 255);
    byte2 = ((valor >> 16) & 255);
    byte3 = ((valor >> 24) & 255);
    if (debug) printf("\e[7;36m%08d\e[0;36m convertido em: \e[7;36m%02x.%02x.%02x.%02x\e[0;36m.\e[0m\n\n"
                      , valor, byte3, byte2, byte1, byte0);
    usleep(delay);
}

int main(int argc, const char * argv[]) {
    
    char entrada[200];
    char comando[8][200];
    char *token = NULL;
    unsigned char ***bitmap = NULL;
    int tamX = 0;
    int tamY = 0;
    int imgCriada = 0;
    int qtddCores = 3;
    FILE *fp = NULL;
    char filePath[200] = "~/Teste/teste.bmp";
    int fileSize = 50;
    char aux = '\0';
    int reservado = 0;
    int deslocamento = 54;
    int resto = 0;
    int planos = 3;
    int bitsPixel = 24;
    int compressao = 0;
    int tamanho = 0;
    int pixelsHorizontal = 0;
    int pixelsVertical = 0;
    
    
    if (argc != 1) { // Se tiver argumentos...
        
        
        if (argc == 2) {
            if (strcmp(argv[1], "?") || strcmp(argv[1], "help")) printf("\n\n\e[36mThis is a BMP image file Compiler.\e[0m\n\n\n");
            return 0;
        }
        if (argc == 3) {
            if ((strcmp(argv[1], "-d") || strcmp(argv[1], "--debug") || strcmp(argv[1], "--DEBUG")) && (strcmp(argv[2], "0") || strcmp(argv[2] ,"1"))) {
                debug = atoi(argv[2]);
                printf("Debug habilitado! Debug: %d\n\n", debug);
            } else {
                printf("\e[31mErro na passagem de parâmetros. Ignorando...\e[0m\n\n");
            }
        }
    }
    
    system("clear");
    printf("\e[36mDigite um commando ou digite \"?\" para listar opções.\e[0m");

    while (1)
    {
        system("clear");
        printf("\n\n\e[7;36m           +--------------------+           \e[0m\n");
        printf("\e[7;36m           |   COMPILADOR BMP   |           \e[0m\n");
        printf("\e[7;36m           +--------------------+           \e[0m\n\n");
        printf("\e[5;36m \e[0m");

        memset(entrada,'\0', 200);
        for (int i = 0; i < 5; i++) memset(comando[i], '\0', 200);

        fgets(entrada, 200, stdin);

        // SEPARA OS PARÂMETROS DA ENTRADA
        token = strtok(entrada, " ");
        int i = 0;
        while (token != NULL) {
            strcpy(comando[i], token);
            token = strtok(NULL, " ");
            i++;
        }

        int comando1 = atoi(comando[1]);
        int comando2 = atoi(comando[2]);
        int comando3 = atoi(comando[3]);
        int comando4 = atoi(comando[4]);
        int comando5 = atoi(comando[5]);
        int comando6 = atoi(comando[6]);
        int comando7 = atoi(comando[7]);

        // CRIANDO IMAGEM
        switch (*comando[0]) {
            case 'c':
            case 'C':
                if (comando1 <= 0) {
                    printf("\e[33mLargura inválida: %d. Digite novamente.\e[0m\n", comando1);
                    sleep(1);
                    break;
                }
                if (comando2 <= 0) {
                    printf("\e[33mAltura inválida: %d. Digite novamente.\e[0m\n", comando2);
                    sleep(1);
                    break;
                }
                
                imgCriada = 1;
                tamX = comando1;
                tamY = comando2;
                
                // DELAY TIME
                delay = (4000000/(2*tamX*tamY));
                if (delay == 0) {
                    delay = 10;
                }
                
                
                
                // ------- CABEÇALHO --------
                
                // Filesize
                if (((tamX*qtddCores)%4) != 0) {            // Se não for múltiplo de 4...
                    fileSize = 4;                           // Adiciona o stuffing bits
                }
                fileSize += 54;                             // Bytes de cabeçalho
                fileSize += tamY*(((tamX*qtddCores)/4)*4);  // Bytes de bitmap
                
                // Reserved
                reservado = 0;
                
                // Deslocamento
                deslocamento = 54;
                
                // Resto
                resto = 6;
                
                // Planos
                planos = qtddCores;
                
                // Bits por Pixel (4 bytes)
                bitsPixel = 24;
                
                // Compressão (Sem compressão)
                compressao = 0;
                
                // Tamanho
                tamanho = tamX*tamY;
                
                // Pixels/m na Horizontal
                pixelsHorizontal = 0;
                
                // Pixels/m na Vertical
                pixelsVertical = 0;
                
                
                if (debug)
                {
                    printf("Criando cabeçalho.\n");
                    printf("Criando imagem de tamanho %dx%d.\e[0m\n", comando1, comando2);
                    printf("tamX: %d, tamY: %d.\n", tamX, tamY);
                    printf("comando1: %d, comando2: %d.\n", comando1, comando2);
                }
                
                // Aloca vetor tridimensional
                bitmap = (unsigned char***) malloc(tamX*sizeof(unsigned char*)); // Aloca vetor com quantidade colunas
                for (int i = 0; i < tamX; i++)
                {
                    bitmap[i] = (unsigned char**) malloc(tamY*sizeof(unsigned char*)); // Aloca vetor com quantidade de linhas.
                    for (int j = 0; j < tamY; j++)
                    {
                        bitmap[i][j] = (unsigned char*) malloc(qtddCores*sizeof(unsigned char*)); // Aloca vetor com quantidade de cores.
                    }
                }
                
                
                
                sleep(1);
                break;
                
            case 'L':
            case 'l':
                if (imgCriada == 0)
                {
                    printf("\e[31mCrie uma imagem antes de modificá-la.\e[0m\n");
                    sleep(1);
                    break;
                }
                if ((comando1 < 0) || (comando1 > 255))
                {
                    printf("Comando[1] = %s\n", comando[1]);
                    printf("Valor de Vermelho inválido: %d.\e[0m\n", comando1);
                    sleep(1);
                    break;
                }
                if ((comando2 < 0) || (comando2 > 255))
                {
                    printf("Valor de Verde inválido: %d.\e[0m\n", comando2);
                    sleep(1);
                    break;
                }
                if ((comando3 < 0) || (comando3 > 255))
                {
                    printf("Valor de Azul inválido: %d.\e[0m\n", comando3);
                    sleep(1);
                    break;
                }
                printf("Preenchendo imagem com a cor (%d,%d,%d)\n",comando1, comando2, comando3);

                
                for (int j = 0; j < tamY; j++) {                // Colunas
                    for (int i = 0; i < tamX; i++) {            // Linhas
                        if (debug) {
                            printf("i: %03d, j: %03d. (\e[31m%03d\e[0m,\e[32m%03d\e[0m,\e[34m%03d\e[0m)\n",
                                   i, j, comando1, comando2, comando3);
                            usleep(delay);
                        }
                        bitmap[i][j][0] = (unsigned char)comando3;       // Azul.
                        bitmap[i][j][1] = (unsigned char)comando2;       // Verde.
                        bitmap[i][j][2] = (unsigned char)comando1;       // Vermelho.
                    }
                }
                sleep(1);
                break;
                
                // Retângulo
            case 'R':
            case 'r':
                if (imgCriada == 0)
                {
                    printf("\e[31mCrie uma imagem antes de modificá-la.\e[0m\n");
                    sleep(1);
                    break;
                }
                if ((comando1 < 0) || (comando1 > tamX))
                {
                    printf("Posição X1 inválida: %d.\e[0m\n", comando1);
                    sleep(1);
                    break;
                }
                if ((comando2 < 0) || (comando2 > tamY))
                {
                    printf("Posição Y1 inválida: %d.\e[0m\n", comando2);
                    sleep(1);
                    break;
                }
                if ((comando3 < 0) || (comando3 > tamX))
                {
                    printf("Posição X2 inválida: %d.\e[0m\n", comando3);
                    sleep(1);
                    break;
                }
                if ((comando4 < 0) || (comando4 > tamY))
                {
                    printf("Posição Y2 inválida: %d.\e[0m\n", comando4);
                    sleep(1);
                    break;
                }
                if ((comando5 < 0) || (comando5 > 255))
                {
                    printf("Valor de Vermelho inválido: %d.\e[0m\n", comando5);
                    sleep(1);
                    break;
                }
                if ((comando6 < 0) || (comando6 > 255))
                {
                    printf("Valor de Verde inválido: %d.\e[0m\n", comando6);
                    sleep(1);
                    break;
                }
                if ((comando7 < 0) || (comando7 > 255))
                {
                    printf("Valor de Azul inválido: %d.\e[0m\n", comando7);
                    sleep(1);
                    break;
                }
                printf("Desenhando retângulo...\e[0m\n");
                sleep(1);
                break;
                
                // Circulo
            case 'B':
            case 'b':
                if (imgCriada == 0)
                {
                    printf("\e[31mCrie uma imagem antes de modificá-la.\e[0m\n");
                    sleep(1);
                    break;
                }
                printf("Desenhando circulo:\e[0m\n");
                sleep(1);
                break;
                
            case 'P':
            case 'p':
                if (imgCriada == 0)
                {
                    printf("\e[31mCrie uma imagem antes de modificá-la.\e[0m\n");
                    sleep(1);
                    break;
                }
                printf("Pintando um pixel:\e[0m\n");
                sleep(1);
                break;
                
            case 'S':
            case 's':
                if (imgCriada == 0)
                {
                    printf("\e[31mCrie uma imagem antes de salvá-la.\e[0m\n");
                    sleep(1);
                    break;
                }
                printf("Salvando imagem com %dx%d.\e[0m\n", tamX, tamY);
                
                if (comando1 == '\0') {
                    fp = fopen("teste.bmp", "w+");
                } else {
                    fp = fopen(filePath, "w+");
                }
                
                sleep(1);

                // --- CABEÇALHO --- //
                fwrite("BM", 1, 2 ,fp);                 // 00-01 type
                
                separaEmBytes(fileSize);
                fwrite(&byte3, 1, 1, fp);               // 02 size
                fwrite(&byte2, 1, 1, fp);               // 03 size
                fwrite(&byte1, 1, 1, fp);               // 04 size
                fwrite(&byte0, 1, 1, fp);               // 05 size
                
                separaEmBytes(reservado);
                fwrite(&byte3, 1, 1, fp);               // 06 zeros
                fwrite(&byte2, 1, 1, fp);               // 07 zeros
                fwrite(&byte1, 1, 1, fp);               // 08 zeros
                fwrite(&byte0, 1, 1, fp);               // 09 zeros
                
                separaEmBytes(deslocamento);
                fwrite(&byte3, 1, 1, fp);               // 10 deslocamento até imagem
                fwrite(&byte2, 1, 1, fp);               // 11 deslocamento até imagem
                fwrite(&byte1, 1, 1, fp);               // 12 deslocamento até imagem
                fwrite(&byte0, 1, 1, fp);               // 13 deslocamento até imagem
                
                separaEmBytes(resto);
                fwrite(&byte3, 1, 1, fp);               // 14 resto do bloco de controle
                fwrite(&byte2, 1, 1, fp);               // 15 resto do bloco de controle
                fwrite(&byte1, 1, 1, fp);               // 16 resto do bloco de controle
                fwrite(&byte0, 1, 1, fp);               // 17 resto do bloco de controle
                
                separaEmBytes(tamX);
                fwrite(&byte3, 1, 1, fp);               // 18 numero de colunas
                fwrite(&byte2, 1, 1, fp);               // 19 numero de colunas
                fwrite(&byte1, 1, 1, fp);               // 20 numero de colunas
                fwrite(&byte0, 1, 1, fp);               // 21 numero de colunas
                
                separaEmBytes(tamY);
                fwrite(&byte3, 1, 1, fp);               // 22 numero de linhas
                fwrite(&byte2, 1, 1, fp);               // 23 numero de linhas
                fwrite(&byte1, 1, 1, fp);               // 24 numero de linhas
                fwrite(&byte0, 1, 1, fp);               // 25 numero de linhas
                
                separaEmBytes(planos);
                fwrite(&byte0, 1, 1, fp);               // 26 planos
                fwrite(&byte1, 1, 1, fp);               // 27 planos
                
                separaEmBytes(bitsPixel);
                fwrite(&byte3, 1, 1, fp);               // 28 bits/pixel
                fwrite(&byte1, 1, 1, fp);               // 29 bits/pixel
                fwrite(&byte3, 1, 1, fp);               // 30 bits/pixel
                fwrite(&byte1, 1, 1, fp);               // 31 bits/pixel
                
                separaEmBytes(compressao);
                fwrite(&byte3, 1, 1, fp);               // 32 compressao
                fwrite(&byte2, 1, 1, fp);               // 33 compressao
                fwrite(&byte1, 1, 1, fp);               // 34 compressao
                fwrite(&byte0, 1, 1, fp);               // 35 compressao
                
                separaEmBytes(tamanho);
                fwrite(&byte3, 1, 1, fp);               // 36 tamanho da imagem
                fwrite(&byte2, 1, 1, fp);               // 37 tamanho da imagem
                fwrite(&byte1, 1, 1, fp);               // 38 tamanho da imagem
                fwrite(&byte0, 1, 1, fp);               // 39 tamanho da imagem
                
                separaEmBytes(pixelsHorizontal);
                fwrite(&byte3, 1, 1, fp);               // 40 pixels/m na horizontal
                fwrite(&byte2, 1, 1, fp);               // 41 pixels/m na horizontal
                fwrite(&byte1, 1, 1, fp);               // 42 pixels/m na horizontal
                fwrite(&byte0, 1, 1, fp);               // 43 pixels/m na horizontal
                
                separaEmBytes(pixelsVertical);
                fwrite(&byte3, 1, 1, fp);               // 44 pixels/m na vertical
                fwrite(&byte2, 1, 1, fp);               // 45 pixels/m na vertical
                fwrite(&byte1, 1, 1, fp);               // 46 pixels/m na vertical
                fwrite(&byte0, 1, 1, fp);               // 47 pixels/m na vertical
                
                fwrite("\0\0\0\0\0\0", 1, 6, fp);       // 48-53 restando até 53 bytes
                
                
                // PRINT BITMAP ON FILE
                
                for (int j = 0; j < tamY; j++) {                // Colunas
                    for (int i = 0; i < tamX; i++) {            // Linhas
                        if (debug) {
                            printf("i: %03d, j: %03d. (\e[31m%03d\e[0m,\e[32m%03d\e[0m,\e[34m%03d\e[0m)\n", i, j, bitmap[i][j][2], bitmap[i][j][1], bitmap[i][j][0]);
                            usleep(delay);
                        }
                            aux = bitmap[i][j][0];
                        fwrite(&aux, 1, 1, fp);
                        aux = bitmap[i][j][1];
                        fwrite(&aux, 1, 1, fp);
                        aux = bitmap[i][j][2];
                        fwrite(&aux, 1, 1, fp);
                    }
                }
                
                
                fclose(fp);
                sleep(1);
                break;
                
            case 'X':
            case 'x':
                free(bitmap);
                system("clear");
                printf("\n\n\e[31m   --- PROGRAMA ENCERRADO ---\e[0m\n\n\n");
                return 0;
                break;
                
            case '?':
                printf("   \e[36mAJUDA:\e[0m\n");
                printf("\t\e[36mCRIAR NOVA IMAGEM:\n");
                printf("\t  \e[36mC <largura> <altura>\n\n");
                
                printf("\t\e[36mPREENCHER IMAGEM:\n");
                printf("\t\e[36m  L <\e[31mvermelho 0-255\e[36m> <\e[32mverde 0-255\e[36m> <\e[34mazul 0-255\e[36m>\n\n");
                
                printf("\t\e[36mCRIAR RETÂNGULO:\n");
                printf("\t\e[36m  R <X1> <Y1> <X2> <Y2> <\e[31mvermelho 0-255\e[36m> <\e[32mverde 0-255\e[36m> <\e[34mazul 0-255\e[36m>\n\n");
                
                printf("\t\e[36mCRIAR CÍRCULO:\n");
                printf("\t\e[36m  B <centro X> <centro Y> <raio> <\e[31mvermelho 0-255\e[36m> <\e[32mverde 0-255\e[36m> <\e[34mazul 0-255\e[36m>\n\n");
                
                
                printf("\t\e[36mPINTAR PIXEL:\n");
                printf("\t\e[36m  P <pixel X> <pixel Y> <\e[31mvermelho 0-255\e[36m> <\e[32mverde 0-255\e[36m> <\e[34mazul 0-255\e[36m>\n\n");
                
                printf("\t\e[36mSALVAR IMAGEM:\n");
                printf("\t\e[36m  S <caminho do arquivo.bmp>\e[36m\n");
                printf("\t\e[36m  S - Salva no diretório atual.\e[36m\n\n");
                
                printf("\t\e[36mFINALIZAR PROGRAMA:\e[36m\n");
                printf("\t\e[36m  X\e[0m\n");
                
                
                fgets(&aux, 2, stdin);
                memset(entrada,'\0', 200);
                break;
                
            default:
                printf("\e[33mComando inválido. Digite novamente.\e[0m\n");
                sleep(1);
                break;
        }
        
    }
    
    return 0;
}