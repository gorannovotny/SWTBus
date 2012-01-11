--/
-- =============================================
-- Author:		<Author,Josip>
-- Create date: <27.10.2011 >
-- Description:	<Vrne podatke izracuna cijene vozne karte>

CREATE FUNCTION Fn_PTTblPodatkiCeneVozneKarte (@VoznaKartaID int, 
                                                      @StupacID     int,      --trebamo za iznimke
                                                      @OdPostajeID  int, 
                                                      @DoPostajeID  int,
                                                      @ZaDan        datetime) --trebamo za cjenik
RETURNS 
	@TmpTable table (     
		                  [_CenaKarte]               [float], 
		                  [_ZnesekPopusta]           [float], 
		                  [_DistancaCenika]          [int],
		                  [_DistancaLinije]          [int],
		                  [_DistancaRelacije]        [int],
		                  [_DomDistanca]             [int],
		                  [_InoDistanca]             [int],
		                  [_JeTarifnaRelacija]       [int],
		                  [_JeCenaPosCenika]         [int]
		                  
	)	
AS  
BEGIN	

   DECLARE @NacinDolocanjaCene int;
   DECLARE @TarifniCenikID     int;
   DECLARE @DomacaDrzava       int;
  
   DECLARE @FiksnaCena         float = 0;
   DECLARE @PoputKarte         float = 0;
   DECLARE @VrstaPopustaID     int   = 0;
   DECLARE @InoUdio            float = 0;
   DECLARE @InoDist            int   = 0;
   DECLARE @DomDist            int   = 0;
   DECLARE @DistancaRelacije   int   = 0;
   DECLARE @DistancaLinije     int   = 0;
   DECLARE @KmLinije           int   = 0;
   
   DECLARE @KilometriCenika    int   = 0;
   DECLARE @VarijantaID        int;
   DECLARE @VozniRedID         int;
   DECLARE @FKartaID           int;
   DECLARE @FCenaKarte         float = 0;
   DECLARE @Firma              int;
   DECLARE @JeTarifnaRelacija  int   = 0;
   DECLARE @JeCenaPosCenika    int   = 0;
   declare @DistLinije         int   = 0; 
   
   set @FCenaKarte = 0;
   set @KilometriCenika = 0;
   
   
   -- najdemo kljucne podatke iz karte
   select @FKartaID           = VK.ID,
          @Firma              = VK.Firma,   
          @NacinDolocanjaCene = Vk.NacinDolocanjaCene, 
          @TarifniCenikID     = VK.TarifniRazredID,
          @FiksnaCena         = VK.FiksnaCena,
          @VrstaPopustaID    = VK.PopustID,
          @KilometriCenika    = VK.KMPogoja from PTKTVozneKarte VK 
   where Vk.ID = @VoznaKartaID;  
   
   if (coalesce(@FKartaID,0)) < 1 -- nema karte, cijena je nula
       GOTO Kraj;

   
   -- najdemo sve kljucne podatke iz stupca voznog reda
   select @VarijantaID = St.VarijantaVRID,  @VozniRedID = ST.VozniRedID from PTStupciVR ST 
   Where ST.ID = @StupacID; 
   -- ako nema tog stupca gotovo
   if Coalesce(@VarijantaID,0) <= 0 
      GOTO Kraj;

   -- izracunamo pune kilometre linije
   set @KmLinije = (coalesce(dbo.FnPTFullDistancaVarVR(@VarijantaID),0)/1000); 

     -- odredimo domacu drzavu, trebamo je za domace i ino kilometre
   select @DomacaDrzava = SKFirma.SifraDrzave from SKFirma where 
          SKFirma.Sifra = @Firma; 
   select @DistancaLinije    =_DistancaLinije, 
          @DistancaRelacije  =_DistancaRelacije, 
          @DomDist           =_DomDistanca,
          @InoDist           =_InoDistanca    from   dbo.Fn_PTTblPodatkiDistanceVarVR(@VarijantaID,
                                                                                      @OdPostajeID, 
                                                                                      @DoPostajeID,
                                                                                      @DomacaDrzava);   

   -- 1. Ako karta ima fiksnu cijenu, vrnemo samo cijenu i kilometre
   if @NacinDolocanjaCene = 2 -- fiksna cijena
   begin
     set @JeCenaPosCenika = 1;   
     set @FCenaKarte = @FiksnaCena;
     set @KilometriCenika = dbo.FnPTKumKmVarVRZaCenik(@VarijantaID,
                                                @OdPostajeID, 
                                                @DoPostajeID);
     GOTO Kraj;
   end;

  -- 2. najdemo varijanto iz stupca
   if @NacinDolocanjaCene = 3 -- fiksni km
   begin
     set @KilometriCenika = @KilometriCenika;
   end

  
  -- kilometri iz varijante postaja iz stupca
   if @NacinDolocanjaCene = 1 -- tarifni daljinar
   begin
     set @JeTarifnaRelacija = 1;   
     set @KilometriCenika =dbo.FnPTKumKmVarVRZaCenik(@VarijantaID,
                                                     @OdPostajeID, 
                                                     @DoPostajeID);
     if Coalesce(@KilometriCenika,0) = 0 
        Goto Kraj;    
   end
         
   -- odredivanje iznimke cijene tarifnog cjenika, odPostaje doPostaje za varijantu ili stupac bez obzira na km
   set @FCenaKarte = dbo.FnPTKTIzjemaTCCenika(@TarifniCenikID,
                                              @ZaDan, 
                                              @VozniRedID,
                                              @VarijantaID,
                                              @StupacID,
                                              @OdPostajeID, 
                                              @DoPostajeID);  
   if Coalesce(@FCenaKarte,0) > 0 
   Begin
      set  @JeCenaPosCenika = 1;   
      set @PoputKarte = dbo.FnPTKTIzracunPopustaKarte(@VrstaPopustaID,
                                                      @FCenaKarte);                                              
      Goto Kraj;    
   End;                                                  

   -- određivanje tarifne cijene na kraju ako nije ni u jednom prijašnjem slučaju nađena      
   set @FCenaKarte = dbo.FnPTKTarifnaCenaCenika(@TarifniCenikID,
                                                @ZaDan, 
                                                @KilometriCenika);                                                  
   set @PoputKarte = dbo.FnPTKTIzracunPopustaKarte(@VrstaPopustaID,
                                                   @FCenaKarte);                                              
 Kraj:    
  if @FCenaKarte > 0 
   Insert into @TmpTable Values(@FCenaKarte, 
                                @PoputKarte,
                                @KilometriCenika*1000, 
                                @DistancaLinije,
                                @DistancaRelacije,
                                @DomDist,
                                @InoDist,
                                @JeTarifnaRelacija,
                                @JeCenaPosCenika); 
   Return;
END


-- =============================================
-- Author:		<Josip >
-- Create date: <Create Date, 08.1.2011>
-- Description:	<Vrne pune kilometri između za varijantu od prve do zadnje postaje>
-- =============================================
ALTER FUNCTION "dbo"."FnPTFullDistancaVarVR"(@VarijantaID  int)
RETURNS int  
AS  
BEGIN
   declare @Res int = 0;
    -- kilometri su suma svih postaja osim prve postaje 
    set @Res = ((Select Sum(DistancaM) from PTPostajeVarijantVR
                 WHERE PTPostajeVarijantVR.VarijantaID =@VarijantaID) 
                - (Select Top 1 DistancaM from PTPostajeVarijantVR
                   WHERE PTPostajeVarijantVR.VarijantaID =@VarijantaID order by ZapSt)
               );                       
 Return @res;
END

-- =============================================
-- Author:		<Josip >
-- Create date: <Create Date, 6.11.2011>
-- Description:	<Vrne tabelu bruto, domacih i ino kilometara za varijantuVR>
-- =============================================
ALTER FUNCTION "dbo"."Fn_PTTblPodatkiDistanceVarVR" (@VarijantaID  int, 
                                                    @OdPostajeID  int, 
                                                    @DoPostajeID  int,
                                                    @DomacaDrzava int)
RETURNS @Tabela table (
		                  [_DistancaLinije]        [int],
		                  [_DistancaRelacije]      [int],
		                  [_DomDistanca]           [int],-- =============================================
-- Author:		<Author,Josip>
-- Create date: <Create Date, 26.10.2011>
-- Description:	<Description, Vrne kilometre za cjenik za varijantu i OdPostaje DoPostaje >
-- =============================================
ALTER FUNCTION "dbo"."FnPTKumDistancaVarVRZaCenik"( @VarijantaID  int, 
                                                    @OdPostajeID  int, 
                                                    @DoPostajeID  int)
RETURNS int  
AS  
BEGIN
	DECLARE @Res int;
	DECLARE @OdZapSt int;
	DECLARE @DOZapSt int;
	declare @TempZapSt int;
	declare @TmpTbl table (DistancaM int)
	
	--postavimo uvjek minimalne - pocetne postaje, ako se postaja pojavljuje vise puta u stupcu
	set @OdZapSt = (select top 1 PVarVr.Zapst from PTPostajeVarijantVR PVarVR
	    Left Join PTPostajeVR PVR ON PVR.ID = PVarVR.NodePostajeVRID 
	    where PVarVr.VarijantaID =  @VarijantaID and 
	          PVR.PostajaID = @OdPostajeID -- =============================================
-- Author:		<Author,Josip>
-- Create date: <Create Date, 26.10.2011>
-- Description:	<Description, Vrne kilometre za cjenik za varijantu i OdPostaje DoPostaje >
-- =============================================
ALTER FUNCTION "dbo"."FnPTKumDistancaVarVRZaCenik"( @VarijantaID  int, 
                                                    @OdPostajeID  int, 
                                                    @DoPostajeID  int)
RETURNS int  
AS  
BEGIN
	DECLARE @Res int;
	DECLARE @OdZapSt int;
	DECLARE @DOZapSt int;
	declare @TempZapSt int;
	declare @TmpTbl table (DistancaM int)
	
	--postavimo uvjek minimalne - pocetne postaje, ako se postaja pojavljuje vise puta u stupcu
	set @OdZapSt = (select top 1 PVarVr.Zapst from PTPostajeVarijantVR PVarVR
	    Left Join PTPostajeVR PVR ON PVR.ID = PVarVR.NodePostajeVRID 
	    where PVarVr.VarijantaID =  @VarijantaID and 
	          PVR.PostajaID = @OdPostajeID 
	    order by PVarVr.ZapSt);

	set @DoZapSt = (select top 1 PVarVr.Zapst from PTPostajeVarijantVR PVarVR
	    Left Join PTPostajeVR PVR ON PVR.ID = PVarVR.NodePostajeVRID 
	    where PVarVr.VarijantaID =  @VarijantaID and 
	          PVR.PostajaID = @DoPostajeID 
	    order by PVarVr.ZapSt);
   
   	-- ako su zamjenjena mjesta moramo obrnuti
	if @OdZapSt > @DoZapSt 
	 begin
       set @TempZapSt = @OdZapSt;
       set @OdZapSt   = @DoZapSt;
       set @DoZapSt   = @TempZapSt;
	 end;-- =============================================
-- Author:		<Author,Josip>
-- Create date: <Create Date, 26.10.2011>
-- Description:	<Description, Vrne kilometre za cjenik za varijantu i OdPostaje DoPostaje >
-- =============================================
ALTER FUNCTION "dbo"."FnPTKumDistancaVarVRZaCenik"( @VarijantaID  int, 
                                                    @OdPostajeID  int, 
                                                    @DoPostajeID  int)
RETURNS int  
AS  
BEGIN
	DECLARE @Res int;
	DECLARE @OdZapSt int;
	DECLARE @DOZapSt int;
	declare @TempZapSt int;
	declare @TmpTbl table (DistancaM int)
	
	--postavimo uvjek minimalne - pocetne postaje, ako se postaja pojavljuje vise puta u stupcu
	set @OdZapSt = (select top 1 PVarVr.Zapst from PTPostajeVarijantVR PVarVR
	    Left Join PTPostajeVR PVR ON PVR.ID = PVarVR.NodePostajeVRID 
	    where PVarVr.VarijantaID =  @VarijantaID and 
	          PVR.PostajaID = @OdPostajeID 
	    order by PVarVr.ZapSt);

	set @DoZapSt = (select top 1 PVarVr.Zapst from PTPostajeVarijantVR PVarVR
	    Left Join PTPostajeVR PVR ON PVR.ID = PVarVR.NodePostajeVRID 
	    where PVarVr.VarijantaID =  @VarijantaID and 
	          PVR.PostajaID = @DoPostajeID 
	    order by PVarVr.ZapSt);
   
   	-- ako su zamjenjena mjesta moramo obrnuti
	if @OdZapSt > @DoZapSt 
	 begin
       set @TempZapSt = @OdZapSt;
       set @OdZapSt   = @DoZapSt;
       set @DoZapSt   = @TempZapSt;
	 end;

   -- logika : sestejemo sve kilometre vozlaod in vozlado
   insert into @tmptbl select PTPostajeVarijantVR.DistancaM from PTPostajeVarijantVR
	            where  PTPostajeVarijantVR.VarijantaID = @VarijantaID and 
	                   PTPostajeVarijantVR.ZapSt >  @OdZapSt and 
	                   PTPostajeVarijantVR.ZapSt <= @DoZapSt and 
	                   Coalesce(PTPostajeVarijantVR.Vozel,0) in 
	                     (select Coalesce(PVarVr.Vozel,0) from PTPostajeVarijantVR PVarVR
	                      where PVarVr.VarijantaID = @VarijantaID and 
	                             ((PVarVr.ZapSt = @OdZapSt) or (PVarVr.ZapSt = @DoZapSt)))
	            order by ZapSt;
	            
   set @res = coalesce((select SUM(Distancam) from @TmpTbl),0);	
	            
   Return @res;
END



   -- logika : sestejemo sve kilometre vozlaod in vozlado
   insert into @tmptbl select PTPostajeVarijantVR.DistancaM from PTPostajeVarijantVR
	            where  PTPostajeVarijantVR.VarijantaID = @VarijantaID and 
	                   PTPostajeVarijantVR.ZapSt >  @OdZapSt and 
	                   PTPostajeVarijantVR.ZapSt <= @DoZapSt and 
	                   Coalesce(PTPostajeVarijantVR.Vozel,0) in 
	                     (select Coalesce(PVarVr.Vozel,0) from PTPostajeVarijantVR PVarVR
	                      where PVarVr.VarijantaID = @VarijantaID and 
	                             ((PVarVr.ZapSt = @OdZapSt) or (PVarVr.ZapSt = @DoZapSt)))
	            order by ZapSt;
	            
   set @res = coalesce((select SUM(Distancam) from @TmpTbl),0);	
	            
   Return @res;
END


	    order by PVarVr.ZapSt);

	set @DoZapSt = (select top 1 PVarVr.Zapst from PTPostajeVarijantVR PVarVR
	    Left Join PTPostajeVR PVR ON PVR.ID = PVarVR.NodePostajeVRID 
	    where PVarVr.VarijantaID =  @VarijantaID and 
	          PVR.PostajaID = @DoPostajeID 
	    order by PVarVr.ZapSt);
   
   	-- ako su zamjenjena mjesta moramo obrnuti
	if @OdZapSt > @DoZapSt 
	 begin
       set @TempZapSt = @OdZapSt;
       set @OdZapSt   = @DoZapSt;
       set @DoZapSt   = @TempZapSt;
	 end;

   -- logika : sestejemo sve kilometre vozlaod in vozlado
   insert into @tmptbl select PTPostajeVarijantVR.DistancaM from PTPostajeVarijantVR
	            where  PTPostajeVarijantVR.VarijantaID = @VarijantaID and 
	                   PTPostajeVarijantVR.ZapSt >  @OdZapSt and 
	                   PTPostajeVarijantVR.ZapSt <= @DoZapSt and 
	                   Coalesce(PTPostajeVarijantVR.Vozel,0) in 
	                     (select Coalesce(PVarVr.Vozel,0) from PTPostajeVarijantVR PVarVR
	                      where PVarVr.VarijantaID = @VarijantaID and 
	                             ((PVarVr.ZapSt = @OdZapSt) or (PVarVr.ZapSt = @DoZapSt)))
	            order by ZapSt;
	            
   set @res = coalesce((select SUM(Distancam) from @TmpTbl),0);	
	            
   Return @res;
END


		                  [_InoDistanca]           [int]		                  
	                    )	  
AS  
BEGIN

declare @DistancaLinije int = 0;
declare @KumM           int = 0;
declare @InoM           int = 0;
declare @OdZapSt        int;
declare @DoZapSt        int;
declare @TempZapSt      int;
declare @Inserted       int;

	
 DECLARE @PomTbl TABLE(ZapSt int PRIMARY KEY, NodeID int, PostajaID int, DistancaM int, Drzava int);
	
 -- nafinalmo vmesnu tabelu	z strukturom
 INSERT @Pomtbl SELECT PVAR.ZapSt, PVAR.NodePostajeVRID, PVR.PostajaID, 
                       PVAR.DistancaM, PST.Drzava FROM PTPostajeVarijantVR PVAR
        inner join PTPostajeVR PVR ON PVAR.NodePostajeVRID = PVR.ID
        inner join PTPostaje PST ON PST.ID = PVR.PostajaID 
        WHERE PVAR.VarijantaID =@VarijantaID  
set @inserted = @@ROWCOUNT;        
 IF @inserted=0
    GOTO Kraj;
		
	-- redni broji idu po redu, id-ji ne, moramo po rednim brojevima
	set @OdZapSt = (select top 1 Zapst from @Pomtbl where PostajaID = @OdPostajeID order by ZapSt); 
	set @DoZapSt = (select top 1 Zapst from @Pomtbl where PostajaID = @DoPostajeID order by ZapSt); 
	
	-- ako su zamjenjena mjesta moramo obrnuti
	if @OdZapSt > @DoZapSt 
	 begin
       set @TempZapSt = @OdZapSt;
       set @OdZapSt   = @DoZapSt;
       set @DoZapSt   = @TempZapSt;
	 end;
    
    -- kilometri su suma svih postaja osim prve postaje 
    set @KumM = Coalesce((Select SUM(DistancaM) from @Pomtbl
	                     where ZapSt > @OdZapSt and ZapSt <= @DoZapSt),0);  
	               
    set @InoM = Coalesce((Select SUM(DistancaM) from @Pomtbl
	                      where ZapSt > @OdZapSt and ZapSt <= @DoZapSt and 
	                      Drzava > 0 and Drzava <> @DomacaDrzava),0); 
	                      
    set @DistancaLinije = ((Select Sum(DistancaM) from @Pomtbl) - 
                           (Select Top 1 DistancaM from @Pomtbl where 1=1 order by ZapSt)
                          );                       

 Kraj:    
  if @inserted > 0 
    insert into @Tabela  Values(@DistancaLinije, @KumM,@KumM-@InoM,@InoM);
  Return;
END

-- =============================================
-- Author:		<Josip >
-- Create date: <Create Date, 26.09.2011>
-- Description:	<Vrne kumulativne kilometri između dvije točke varijante VR>
-- =============================================
ALTER FUNCTION "dbo"."FnPTKumKmVarVRZaCenik"(@VarijantaID  int, 
                                             @OdPostajeID  int, 
                                             @DoPostajeID  int)
RETURNS int  
AS  
BEGIN
   Declare @res int;
   Set @Res = cast((dbo.FnPTKumDistancaVarVRZaCenik(@VarijantaID,@OdPostajeID,@DoPostajeID)/1000) as int);   

   Return @res;
END


-- =============================================
-- Author:		<Author,Josip>
-- Create date: <Create Date, 26.10.2011>
-- Description:	<Description, Vrne kilometre za cjenik za varijantu i OdPostaje DoPostaje >
-- =============================================
ALTER FUNCTION "dbo"."FnPTKumDistancaVarVRZaCenik"( @VarijantaID  int, 
                                                    @OdPostajeID  int, 
                                                    @DoPostajeID  int)
RETURNS int  
AS  
BEGIN
	DECLARE @Res int;
	DECLARE @OdZapSt int;
	DECLARE @DOZapSt int;
	declare @TempZapSt int;
	declare @TmpTbl table (DistancaM int)
	
	--postavimo uvjek minimalne - pocetne postaje, ako se postaja pojavljuje vise puta u stupcu
	set @OdZapSt = (select top 1 PVarVr.Zapst from PTPostajeVarijantVR PVarVR
	    Left Join PTPostajeVR PVR ON PVR.ID = PVarVR.NodePostajeVRID 
	    where PVarVr.VarijantaID =  @VarijantaID and 
	          PVR.PostajaID = @OdPostajeID 
	    order by PVarVr.ZapSt);

	set @DoZapSt = (select top 1 PVarVr.Zapst from PTPostajeVarijantVR PVarVR
	    Left Join PTPostajeVR PVR ON PVR.ID = PVarVR.NodePostajeVRID 
	    where PVarVr.VarijantaID =  @VarijantaID and 
	          PVR.PostajaID = @DoPostajeID 
	    order by PVarVr.ZapSt);
   
   	-- ako su zamjenjena mjesta moramo obrnuti
	if @OdZapSt > @DoZapSt 
	 begin
       set @TempZapSt = @OdZapSt;
       set @OdZapSt   = @DoZapSt;
       set @DoZapSt   = @TempZapSt;
	 end;

   -- logika : sestejemo sve kilometre vozlaod in vozlado
   insert into @tmptbl select PTPostajeVarijantVR.DistancaM from PTPostajeVarijantVR
	            where  PTPostajeVarijantVR.VarijantaID = @VarijantaID and 
	                   PTPostajeVarijantVR.ZapSt >  @OdZapSt and 
	                   PTPostajeVarijantVR.ZapSt <= @DoZapSt and 
	                   Coalesce(PTPostajeVarijantVR.Vozel,0) in 
	                     (select Coalesce(PVarVr.Vozel,0) from PTPostajeVarijantVR PVarVR
	                      where PVarVr.VarijantaID = @VarijantaID and 
	                             ((PVarVr.ZapSt = @OdZapSt) or (PVarVr.ZapSt = @DoZapSt)))
	            order by ZapSt;
	            
   set @res = coalesce((select SUM(Distancam) from @TmpTbl),0);	
	            
   Return @res;
END

-- =============================================
-- Author:		<Josip >
-- Create date: <Create Date, 23.11.2011>
-- Description:	<Izracuna popust karte>
-- =============================================
ALTER FUNCTION "dbo"."FnPTKTIzracunPopustaKarte" ( @VrstaPopustaID int, 
                                                    @PCenaKarte float)
RETURNS float  
AS  
BEGIN
  declare @Result        float =0;
  if @VrstaPopustaID < 1 
     goto Kraj;

  declare @PopustID      int =0;
  declare @NacinIzracuna int =0;
  declare @Vrednost      float =0;

     
    select @PopustID       = coalesce(VP.ID,0),
           @NacinIzracuna  = VP.NacinIzracuna, 
           @Vrednost       = VP.Vrednost from PTKTVrstePopustov VP 
   where VP.ID = @VrstaPopustaID;  
   if @PopustID < 1 
      goto Kraj;
   if @NacinIzracuna = 0 
      set @Result = @PCenaKarte * (@Vrednost/100)
   else 
      set @Result = @Vrednost;
   
 Kraj:  
   return  @Result;
END

-- =============================================
-- Author:		<Josip >
-- Create date: <Create Date, 28.09.2011>
-- Description:	<Vrne tarifnu cijenu za TarifniCjenik i Kilometre na dan>
-- =============================================
ALTER FUNCTION "dbo"."FnPTKTarifnaCenaCenika"(@TarifniCenikID  int, 
                                              @ZaDan  datetime, 
                                              @ZaKilometre  int)
RETURNS float  
AS  
BEGIN
   Declare @Cena float;
   set @Cena = (Select Top 1 TC.Cena from PTKTTarifniRazrediCenik TC 
                Where  TC.IDRazreda=@TarifniCenikID and TC.VeljaOd < (@ZaDan + 1) and 
                TC.OdKM <= @ZaKilometre
                order by TC.VeljaOd desc, TC.OdKM desc);   
   Return @Cena;
END









