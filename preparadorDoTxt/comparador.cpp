#include "preparador.h"
#include <iostream>
#include <fstream>
#include <iostream>
#include "comparador.h"

using namespace std;

comparador::comparador() {
    int contadorA = 878, contadorN = 794, j =0, k = 0, indicador=0;
    string linhaA, linhaN;
    size_t finalAstart, finalAend, finalNstart, finalNend;

    somador = 0;
    finalAstart = 0;
    finalNstart = 0;

    Antigo.open("indice2017.txt");
    Novo.open("indice2018.txt");

    SaidaCerteza.open("indiceCerteza.txt");
    SaidaIncerta.open("indiceIncerto.txt");

    string antigo { istreambuf_iterator<char>(Antigo), istreambuf_iterator<char>() };
    string novo { istreambuf_iterator<char>(Novo), istreambuf_iterator<char>() };

    if(SaidaCerteza.is_open() && SaidaIncerta.is_open()) {
        while(j<contadorA) {
            finalAend = antigo.find("\n", finalAstart);
            linhaA = antigo.substr(finalAstart, (finalAend-finalAstart));
            finalAstart=finalAend+1;

            finalA = linhaA.find("-");
            antigoSN = linhaA.substr(finalA);

            while(k<contadorN) {
                finalNend = novo.find("\n", finalNstart);
                linhaN = novo.substr(finalNstart, (finalNend-finalNstart));
                finalNstart=finalNend+1;

                finalN = linhaN.find("-");
                novoSN = linhaN.substr(finalN);

                if(antigoSN == novoSN) {
                    SaidaCerteza << linhaA << "  " << linhaN << "\n";
                    indicador = 1;
                }
                k++;
            }
            if(indicador == 0)
                SaidaCerteza << linhaA << " NADICADENADA \n";

            indicador = 0;
            finalNstart = 0;
            k=0;
            j++;
        }
        j=0;
        k=0;
        indicador =0;
        finalAstart =0;
        finalNstart = 0;


        while(k<contadorN) {
            finalNend = novo.find("\n", finalNstart);
            linhaN = novo.substr(finalNstart, (finalNend-finalNstart));
            finalNstart=finalNend+1;

            finalN = linhaN.find("-");
            novoSN = linhaN.substr(finalN);

            while(j<contadorA) {
                finalAend = antigo.find("\n", finalAstart);
                linhaA = antigo.substr(finalAstart, (finalAend-finalAstart));
                finalAstart=finalAend+1;

                finalA = linhaA.find("-");
                antigoSN = linhaA.substr(finalA);

                if(novoSN == antigoSN)
                    indicador = 1;
                j++;
            }
            if(indicador == 0)
                SaidaCerteza << linhaN << " NANANINANAO \n";

            indicador = 0;
            finalAstart = 0;
            j=0;
            k++;
        }



    }

    Antigo.close();
    Novo.close();
    SaidaCerteza.close();
    SaidaIncerta.close();


}
