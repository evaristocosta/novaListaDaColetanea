#include "preparador.h"
#include <iostream>
#include <fstream>

using namespace std;

preparador::preparador() {

    Texto.open("coletanea2018.txt");
    Indice.open("indice2018.txt");
    if(Texto.is_open()) {
        while(getline(Texto, item)) {
            if(isdigit(item[0])) {
                Indice << item;
                Indice << "\n";
            }
        }
    }
    Texto.close();
    Indice.close();
}
