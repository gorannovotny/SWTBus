SELECT * FROM PTVozniRedi WHERE PTVozniRedi.VeljaDo < "2011-12-31 00:00:00";

SELECT * FROM PTVarijanteVR
WHERE PTVarijanteVR.VozniRedID NOT IN( SELECT ID FROM PTVozniRedi);
DELETE FROM PTVarijanteVR
WHERE PTVarijanteVR.VozniRedID NOT IN( SELECT ID FROM PTVozniRedi);

SELECT COUNT(*) FROM PTStupciVR WHERE PTStupciVR.VarijantaVRID NOT IN
 ( SELECT ID FROM PTVarijanteVR);
DELETE FROM PTStupciVR WHERE PTStupciVR.VarijantaVRID NOT IN
 ( SELECT ID FROM PTVarijanteVR);

SELECT * FROM PTStupciVR WHERE PTStupciVR.VozniRedID NOT IN (SELECT ID FROM PTVozniRedi); 
 
SELECT * FROM PTPostajeVarijantVR; 
SELECT COUNT(*) FROM PTPostajeVarijantVR WHERE PTPostajeVarijantVR.VarijantaID NOT IN 
  (SELECT ID FROM PTVarijanteVR);
DELETE FROM PTPostajeVarijantVR WHERE PTPostajeVarijantVR.VarijantaID NOT IN 
  (SELECT ID FROM PTVarijanteVR);

SELECT * FROM PTPostajeVarijantVR WHERE PTPostajeVarijantVR.NodePostajeVRID NOT IN (SELECT ID FROM PTPostajeVR);
DELETE FROM PTPostajeVarijantVR WHERE PTPostajeVarijantVR.NodePostajeVRID NOT IN (SELECT ID FROM PTPostajeVR);

SELECT COUNT(*) FROM PTPostajeVR WHERE PTPostajeVR.VozniRedID NOT IN 
 (SELECT ID FROM PTVozniRedi);
DELETE FROM PTPostajeVR WHERE PTPostajeVR.VozniRedID NOT IN 
 (SELECT ID FROM PTVozniRedi);
 
DELETE FROM PTPostaje WHERE ID NOT IN 
  (SELECT PTPostajeVR.PostajaID FROM PTPostajeVR GROUP BY 1);
 
SELECT COUNT(*) FROM PTCasiVoznjeVR WHERE PTCasiVoznjeVR.StupacVRID NOT IN
 ( SELECT ID FROM PTStupciVR);
DELETE FROM PTCasiVoznjeVR WHERE PTCasiVoznjeVR.StupacVRID NOT IN
 ( SELECT ID FROM PTStupciVR);
 
SELECT * FROM PTStupciVR;
SELECT PTVozniRedi.Opis1, ptpostajevarijantvr.id,PTPostajeVR.ZapSt,ptpostaje.naziv,PTPostajeVR.DistancaM
,CAST(PTCasiVoznjeVR.VremeOdhoda * 24 AS INT),CAST((PTCasiVoznjeVR.VremeOdhoda * 24 - CAST(PTCasiVoznjeVR.VremeOdhoda * 24 AS INT))*60 AS INT)
FROM PTPostaje,PTPostajeVR,PTPostajeVarijantVR,PTVarijanteVR,PTStupciVR,PTVozniRedi,PTCasiVoznjeVR
WHERE PTPostajeVR.postajaid = PTPostaje.id
AND   PTPostajeVarijantVR.NodePostajeVRID = PTPostajeVR.ID
AND   PTVarijanteVR.id = PTPostajeVarijantVR.VarijantaID
AND   PTStupciVR.VarijantaVRID = PTVarijanteVR.id
AND   PTVarijanteVR.VozniRedID = PTVozniRedi.ID
AND   PTCasiVoznjeVR.NodePostajeVarijanteVRID = PTPostajeVarijantVR.ID
AND   PTCasiVoznjeVR.StupacVRID = PTStupciVR.ID
AND   PTStupciVR.ID = 24772
ORDER BY 1;
SELECT * FROM PTPostajeVarijantVR ;
SELECT * FROM PTKTVozneKarte;

SELECT * FROM PTStupciVR
;
--create index ix1 on PTPostajeVR(postajaid);
--create index ix2 on PTPostajeVarijantVR(NodePostajeVRID);
--create index ix3 on PTPostajeVarijantVR(VarijantaID);
--create index ix4 on PTStupciVR(VarijantaVRID);

vacuum