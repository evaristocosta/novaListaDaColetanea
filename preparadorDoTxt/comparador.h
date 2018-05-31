#ifndef COMPARADOR_H
#define COMPARADOR_H
#include <fstream>

using namespace std;

class comparador {
public:
    comparador();

private:
    ifstream Antigo, Novo;
    ofstream SaidaCerteza, SaidaIncerta;
    string antigoSN, novoSN;

    size_t finalN, finalA;
    int somador, i;
};



#endif //COMPARADOR_H
