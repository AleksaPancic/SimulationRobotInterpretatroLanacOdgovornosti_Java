#include<iostream>
#include<vector>

using namespace std;

class Robot;

//interpretator
class Komanda;
class Kretanje
{
private:
    vector<int> kretanje;
    Robot robot;
public:
    Kretanje() {}
    virtual ~Kretanje()
    {
    kretanje.clear();
    delete kretanje;
    }
    virtual void Start(const int& r, const int& c, /*Komanda* Komanda*/);
    virtual void Move(const char& dir);
    virtual void End();
};

class Komanda{
public:
    virtual ~Komanda(){}
    virtual void Interpretiraj(Kretanje &Kretanje);
};

void Kretanje::Start(const int& r, const int& c)
{
    //uradi nesto
}
void Kretanje::Move(const char& dir)
{
    switch(dir){
        //robot nije uradjen
        case 'U' : kretanje.push_back(robot->pomerisenagore(int x, int y));
        case 'D' : kretanje.push_back(robot->pomerisenadole(int x, int y));
        case 'L' : kretanje.push_back(robot->pomerisenalevo(int x, int y));
        case 'R' : kretanje.push_back(robot->pomerisenadesno(int x, int y));
        default: throw("Wrong input");
    }
}
void Kretanje::End()
{
    for(vector<int>::iterator it = kretanje.begin(); it!=kretanje.end(); ++it){
        delete *it;
        kretanje.clear();
        robot.ugasi();
    }
}

class StartKomanda : public Komanda{
private:
    int r;
    int c;
    Robot robot;
    public:
        StartKomanda(int x, int y) : r(x), y(c){}
        virtual ~StartKomanda(){}
        virtual void Interpretiraj(Kretanje &Kretanje){
            cout <<"Start na kordinatama r " << r << " i c: " << c << endl;
            Kretanje.Start(x,y);
            robot.reset();
        }
};

class EndKomanda: public Komanda{
public:
    virtual ~EndKomanda(){}
    virtual void Interpretiraj(Kretanje &Kretanje){
        cout << "End interpret" << endl;
        Kretanje.End();
    }
};

class MoveKomanda : public Komanda{
private:
    char z;
public:
    virtual ~MoveKomada(){}
    virtual void Interpretiraj(Kretanje &Kretanje){
        cout << "Znak " << z << endl;
        Kretanje.Move(z);
    }
};
//lanac odgovornosti
class Senzor;
class SenzorHandler
{
protected:
    SenzorHandler* naredni;
    Senzor* senzor;
    public:
        SenzorHandler(Senzor* senzorr): naredni(NULL), senzor(senzorr) {}
        virtual ~SenzorHandler() {
        if (naredni != NULL) {
            delete naredni;
        }
        }

        virtual void DodajHandler(SenzorHandler* novi){
            if (naredni == NULL) {
            naredni = novi;
        } else {
            naredni->DodajHandler(novi);
        }
        }
        //metode su na kraju koda uradjene
        virtual void TestPolje(int a, int b);
        virtual void pokrenisezax(int c);
        virtual void pokrenisezay(int d);
};
class CSenzorHandler
{
protected:
    SenzorHandler* trenutni;
    Senzor* senzor;
    int x;
    int y;
public:
    CSenzorHandler(Senzor* m_senzor, int a, int b) : senzor(m_senzor), x(a), y(b){}
    virtual ~CSenzorHandler(){}
    virtual void TestPolje(int a, int b);
    virtual void pokrenisezax(int c);
    virtual void pokrenisezay(int d);
};

class Senzor
{
protected:
    //prepreka na [x] i [y]
    int x;
    int y;
    int trenutnopoljex;
    int trenutnopoljey;
    SenzorHandler* senzor;
public:
    Senzor(int a, int b) : x(a), y(b) , trenutnopoljex(0), trenutnopoljey(0){
        senzor = new CSenzorHandler(this);
        senzor->DodajHandler(new CSenzorHandler(this, x, y));
    }
    virtual ~Senzor(){
        delete senzor;
    }
    virtual int dajx(){
        return x;
    }
    virtual int dajy(){
        return y;
    }
    virtual int vratitrenutnox(){
        return trenutnopoljex;
    }
    virtual int vratetrenutnoy(){
        return trenutnopoljey;
    }
    virtual void TestPolje(int a, int b);
    virtual void pokrenisezax(int c);
    virtual void pokrenisezay(int d);
};



void SenzorHandler::TestPolje(int a, int b){
        if(senzor->dajx() == a && senzor->dajy() == b){
                cout << "Ne mogu ovde da prodjem" << endl;
        }
        else{
            naredni->TestPolje(a,b);
        }
}
void SenzorHandler::pokrenisezax(int c)
{
    if(senzor->trenutnopoljex >= 0 && senzor->trenutnopoljey >=0 )
    {
        senzor->pokrenisezax(c)
    }
    else throw("Out of bounds");
}

void SenzorHandler::pokrenisezay(int d)
{
        if(senzor->trenutnopoljex >= 0 && senzor->trenutnopoljey >=0 )
    {
            senzor->pokrenisezay(d);
    }
    else throw("Out of bounds");
}

