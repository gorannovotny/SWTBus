CREATE TABLE PTPostaje
    (
        ID INT NOT NULL,
        Sifra VARCHAR NOT NULL,
        Naziv VARCHAR,
        VrstaPostaje INT,
        Drzava INT,
        PGRID INT,
        PRIMARY KEY(ID)
    );
    
CREATE TABLE PTVozniRedi (
        ID INT NOT NULL,
        Firma INT NOT NULL,
        Sifra VARCHAR NOT NULL,
        OznakaLinije VARCHAR,
        PrivitakDozvole VARCHAR,
        Registracija VARCHAR,
        Opis1 VARCHAR,
        Opis2 VARCHAR,
        VeljaOd DATETIME,
        VeljaDo DATETIME,
        VrstaVR INT,
        KategorijaPrevoza INT,
        VrstaLinije INT,
        VrstaPrevoza INT,
        NacinPrevoza INT,
        VrstaPosadeID INT,
        OE INT,
        PrevoznikID INT,
        Kooperacija INT,
        Pool INT,
        DOSVRID INT,
        PRIMARY KEY(ID)
    );
    
CREATE TABLE PTPostajeVR(
        ID INT NOT NULL,
        VozniRedID INT NOT NULL REFERENCES PTVozniRedi(ID),
        ZapSt INT NOT NULL,
        Vozel INT NOT NULL,
        PostajaID INT NOT NULL REFERENCES PTPostaje(ID),
        KumDistancaM INT,
        DistancaM INT,
        Staje CHAR,
        DosID INT,
        PRIMARY KEY(ID)
   );

CREATE TABLE PTVarijanteVR (
        ID INT NOT NULL,
        VozniRedID INT NOT NULL REFERENCES PTVozniRedi(ID),
        Varijanta INT NOT NULL,
        Opis1 VARCHAR,
        Opis2 VARCHAR,
        DOSVarID INT,
        PRIMARY KEY(ID)
   );

CREATE TABLE PTPostajeVarijantVR (
        ID INT NOT NULL,
        VarijantaID INT NOT NULL REFERENCES PTVarijanteVR(ID),
        NodePostajeVRID INT NOT NULL REFERENCES PTPostajeVR(ID),
        ZapSt INT,
        KumDistancaM INT,
        DistancaM INT,
        Vozel INT,
        Staje CHAR,
        DOSID INT,
        PRIMARY KEY(ID)
    );
    
CREATE TABLE PTStupciVR
    (
        ID INT NOT NULL,
        Firma INT NOT NULL,
        VozniRedID INT NOT NULL,
        VarijantaVRID INT NOT NULL REFERENCES PTVarijanteVR(ID),
        ZapSt VARCHAR NOT NULL,
        SmerVoznje VARCHAR,
        DneviVoznjeID INT,
        PrevoznikID INT,
        VrstaPrevoza INT,
        VrstaBusa INT,
        NacinPrevoza INT,
        VrstaPosadeID INT,
        VremeOdhoda FLOAT,
        OE INT,
        VeljaOd DATETIME,
        VeljaDo DATETIME,
        StatusERR INT,
        DOSID INT,
        PRIMARY KEY(ID)
    );

CREATE TABLE PTCasiVoznjeVR
    (
        ID INT NOT NULL,
        Firma INT NOT NULL,
        StupacVRID INT NOT NULL REFERENCES PTStupciVR(ID),
        NodePostajeVarijanteVRID INT NOT NULL REFERENCES PTPostajeVarijantVR(ID),
        VremeOdhoda FLOAT,
        VremePrihoda FLOAT,
        PRIMARY KEY(ID)
    );    
    
 CREATE TABLE PTKTProdaja
    (
        ID INT NOT NULL,
        Firma INT NOT NULL,
        DokumentProdajeID INT,
        VrsticaProdajeID INT,
        DokumentBlagajneID INT,
        BUSProdajaID INT,
        Datum DATETIME,
        Vreme DATETIME,
        VoznaKartaID INT,
        Code VARCHAR,
        BRVoznji INT,
        Cena FLOAT,
        Popust1ID INT,
        Popust2ID INT,
        Popust3ID INT,
        PCenaKarte FLOAT,
        NCenaKarte FLOAT,
        Popust FLOAT,
        ZaPlatiti FLOAT,
        PorezProcent INT,
        ProdajnoMestoID INT,
        PrevoznikID INT,
        VrstaPosadeID INT,
        Vozac1ID INT,
        Vozac2ID INT,
        Vozac3ID INT,
        Blagajnik INT,
        Blagajna INT,
        StupacID INT,
        OdPostajeID INT,
        DoPostajeID INT,
        VoziloID INT,
        Rezervacija INT,
        StatusZK INT,
        KmLinijeVR INT,
        KmDomaci INT,
        KmIno INT,
        BRPutnika INT,
        BRKarata INT,
        MobStrojID INT,
        GUID VARCHAR,
        PRIMARY KEY(ID)
    );

CREATE TABLE PTKTVozneKarte
    (
        ID INT NOT NULL,
        Firma INT NOT NULL,
        Sifra VARCHAR NOT NULL,
        TipKarteID INT NOT NULL,
        TarifniRazredID INT,
        Opis VARCHAR,
        StVoznji INT,
        SmerVoznje INT,
        VeljaOdDatuma DATETIME,
        VeljaDoDatuma DATETIME,
        VeljaDniOdProdaje INT,
        Status INT,
        PrevoznikID INT,
        NacinDolocanjaCene INT,
        KMPogoja INT,
        FiksnaCena FLOAT,
        PopustID INT,
        PopustProcent FLOAT,
        SifraValute INT,
        PRIMARY KEY(ID)
    );

CREATE TABLE PTKTTarifniRazrediCenik
    (
        ID INT NOT NULL ,
        Firma INT NOT NULL,
        IDRazreda INT NOT NULL,
        VeljaOd DATETIME NOT NULL,
        OdKM INT NOT NULL,
        Cena FLOAT(53),
       PRIMARY KEY(ID)
    ) ;
    
CREATE TABLE PTKTVrstePopustov
    (
        ID INT NOT NULL ,
        Firma INT NOT NULL,
        Sifra INT NOT NULL,
        Oznaka VARCHAR(10) NOT NULL,
        Opis VARCHAR(50),
        NacinIzracuna INT,
        Vrednost FLOAT(53),
        PRIMARY KEY(ID)
    ) ;
    
CREATE TABLE PTIzjemeCenikaVR
    (
        ID INT NOT NULL ,
        Firma INT NOT NULL,
        TarifniCenikID ,
        VeljaOd DATETIME NOT NULL,
        VozniRedID INT NOT NULL,
        VarijantaID INT NOT NULL ,
        StupacID INT NOT NULL ,
        Postaja1ID INT NOT NULL,
        Postaja2ID INT NOT NULL,
        Cena FLOAT(53),
        PRIMARY KEY(ID)
    ) ;
    
CREATE TABLE PTVozaci
    (
        ID INT NOT NULL ,
        PGRID INT NOT NULL,
        Sifra INT NOT NULL,
        Naziv VARCHAR(40) ,
        OsebaID INT,
        JeVozac INT,
        JeKondukter INT,
        PRIMARY KEY(ID)
    ) ;