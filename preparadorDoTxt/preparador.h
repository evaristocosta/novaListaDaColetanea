#ifndef PREPARADOR_H
#define PREPARADOR_H
#include <fstream>

using namespace std;

class preparador {
public:
    preparador();

private:
    string item;
    ifstream Texto;
    ofstream Indice;
};


#endif //PREPARADOR_H
