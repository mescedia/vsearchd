

CREATE TABLE `4_backend` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `vsdRecordKey` varchar(40) NOT NULL,
  `vsdID` int(11) NOT NULL,
  `vsdTitle` tinytext,
  `vsdName` tinytext,
  `vsdDescription` text,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `rkUNI` (`vsdRecordKey`) USING BTREE
) ;

