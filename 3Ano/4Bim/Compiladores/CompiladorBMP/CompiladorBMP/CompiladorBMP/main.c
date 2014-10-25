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
    char *token;
    char *vetorVermelho;
    char *vetorVerde;
    char *vetorAzul;
    int tamX = 0;
    int tamY = 0;
    int imgCriada = 0;
    
    if (argc != 1) { // Se tiver argumentos...
        if (strcmp(argv[2], "?") || strcmp(argv[2], "help")) printf("This is a BMP image file Compiler.");
    }
    
    system("clear");
    printf("\e[5;34mDigite um commando ou digite \"?\" para listar opções.\e[0m");
    sleep(3);

    while (1)
    {
        system("clear");
        printf("\n\n\e[7;36m        --- COMPILADOR BMP ---        \e[0m\n\n");
        printf("\e[5;36m \e[0m");

        memset(entrada,'\0', 200);
        for (int i = 0; i < 5; i++) memset(comando[i], '\0', 200);

        fgets(entrada, 200, stdin);

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
                printf("Criando imagem de tamanho %dx%d.\e[0m\n", comando1, comando2);
                
                printf("Criando cabeçalho.\n");
                
                
                
                
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
                if ((comando1 >= 0) && (comando1 <= 255))
                {
                    printf("Valor de Vermelho inválido: %d.\e[0m\n", comando1);
                    sleep(1);
                    break;
                }
                if ((comando2 >= 0) && (comando2 <= 255))
                {
                    printf("Valor de Verde inválido: %d.\e[0m\n", comando2);
                    sleep(1);
                    break;
                }
                if ((comando3 >= 0) && (comando3 <= 255))
                {
                    printf("Valor de Azul inválido: %d.\e[0m\n", comando3);
                    sleep(1);
                    break;
                }
                printf("Limpando imagem:\e[0m\n");
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
                if ((comando1 >= 0) && (comando1 <= tamX))
                {
                    printf("Posição X1 inválida: %d.\e[0m\n", comando1);
                    sleep(1);
                }
                if ((comando2 >= 0) && (comando2 <= tamY))
                {
                    printf("Posição Y1 inválida: %d.\e[0m\n", comando2);
                    sleep(1);
                }
                if ((comando3 >= 0) && (comando3 <= tamX))
                {
                    printf("Posição X2 inválida: %d.\e[0m\n", comando3);
                    sleep(1);
                }
                if ((comando4 >= 0) && (comando4 <= tamY))
                {
                    printf("Posição Y2 inválida: %d.\e[0m\n", comando4);
                    sleep(1);
                }
                if ((comando5 >= 0) && (comando5 <= 255))
                {
                    printf("Valor de Vermelho inválido: %d.\e[0m\n", comando5);
                    sleep(1);
                    break;
                }
                if ((comando6 >= 0) && (comando6 <= 255))
                {
                    printf("Valor de Verde inválido: %d.\e[0m\n", comando6);
                    sleep(1);
                    break;
                }
                if ((comando7 >= 0) && (comando7 <= 255))
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
                printf("Salvando imagem:\e[0m\n");
                sleep(1);
                break;
                
            case 'X':
            case 'x':
                printf("\e[31m   --- PROGRAMA ENCERRADO ---\e[0m\n\n\n");
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
