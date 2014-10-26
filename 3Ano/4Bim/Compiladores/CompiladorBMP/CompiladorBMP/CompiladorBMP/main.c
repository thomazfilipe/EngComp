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
    char aux0 = '\0';
    char aux1 = '\0';
    int qtddColunas = 0;
    int qtddLinhas = 0;
    
    
    if (argc != 1) { // Se tiver argumentos...
        if (strcmp(argv[2], "?") || strcmp(argv[2], "help")) printf("This is a BMP image file Compiler.");
    }
    
    system("clear");
    printf("\e[34mDigite um commando ou digite \"?\" para listar opções.\e[0m");

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
                printf("Criando cabeçalho.\n");
                tamX = comando1;
                tamY = comando2;
                
                printf("Criando imagem de tamanho %dx%d.\e[0m\n", comando1, comando2);
                printf("tamX: %d, tamY: %d.\n", tamX, tamY);
                printf("comando1: %d, comando2: %d.\n", comando1, comando2);
                
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
                        printf("i: %d, j: %d. (%d,%d,%d)\n", i, j, comando1, comando2, comando3);
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

                fwrite("BM", 1, 2 ,fp);                 // 00-01 type
                fwrite("\0\0\0\0", 1, 4, fp);           // 02-05 size
                fwrite("\0\0", 1, 2, fp);               // 06-07 zeros
                fwrite("\0\0", 1, 2, fp);               // 08-09 zeros
                fwrite("\0\0\0T", 1, 4, fp);            // 10-13 deslocamento até imagem
                fwrite("\0\0\0\0", 1, 4, fp);           // 14-17 resto do bloco de controle
                aux1 = tamX;
                fwrite("\0\0\0", 1, 3, fp);             // 18-20 numero de colunas1
                fwrite(&aux1, 1, 1, fp);                // 21 numero de colunas2
                aux1 = tamY;
                fwrite("\0\0\0", 1, 3, fp);             // 22 numero de linhas
                fwrite(&aux1, 1, 2, fp);                // 25 numero de linhas
                aux1 = 3;
                fwrite("\0", 1, 1, fp);                 // 26 planos
                fwrite(&aux1, 1, 1, fp);                // 27 planos
                aux1 = 24;
                fwrite("\0\0\0", 1, 3, fp);             // 28-30 bits/pixel
                fwrite(&aux1, 1, 2, fp);                // 31 bits/pixel
                
                fwrite("\0\0\0\0", 1, 4, fp);           // 32-35 compressao
                
                fwrite("\0\0\0\0", 1, 4, fp);           // 36-39 tamanho da imagem
                fwrite("\0\0\0\0", 1, 4, fp);           // 40-43 pixels/m na horizontal
                fwrite("\0\0\0\0", 1, 4, fp);           // 40-43 pixels/m na vertical
                fwrite("\0\0\0\0\0\0", 1, 6, fp);       // 48-54 restando até 54 bytes
                
                // PRINT BITMAP ON FILE
                
                for (int j = 0; j < tamY; j++) {                // Colunas
                    for (int i = 0; i < tamX; i++) {            // Linhas
                        printf("i: %d, j: %d. (%d,%d,%d)\n", i, j, bitmap[i][j][2], bitmap[i][j][1], bitmap[i][j][0]);
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
                printf("Ajuda:\e[0m\n");
                sleep(1);
                break;
                
            default:
                printf("Comando inválido. Digite novamente.\e[0m\n");
                sleep(1);
                break;
        }
        
    }
    
    return 0;
}
