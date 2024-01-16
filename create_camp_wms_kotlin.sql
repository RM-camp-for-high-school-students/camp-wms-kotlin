--
-- Table structure for table `bom`
--

DROP TABLE IF EXISTS `bom`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bom` (
  `goodsID_05` char(6) NOT NULL,
  `goodsType` varchar(20) NOT NULL,
  `goodsName` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`goodsID_05`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rm_group`
--

DROP TABLE IF EXISTS `rm_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rm_group` (
  `groupID` char(5) NOT NULL,
  `groupName` varchar(40) NOT NULL,
  PRIMARY KEY (`groupID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `group_members`
--

DROP TABLE IF EXISTS `group_members`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `group_members` (
  `memberID` char(20) NOT NULL,
  `groupID` char(5) NOT NULL,
  PRIMARY KEY (`memberID`),
  KEY `fk_group` (`groupID`),
  CONSTRAINT `fk_group` FOREIGN KEY (`groupID`) REFERENCES `rm_group` (`groupID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rm_goods`
--

DROP TABLE IF EXISTS `rm_goods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rm_goods` (
  `goodsID` char(10) NOT NULL,
  `goodsType` varchar(20) NOT NULL,
  `goodsName` varchar(50) NOT NULL,
  `isRemoved` tinyint(1) NOT NULL,
  `isAvailable` tinyint(1) NOT NULL,
  `goodsID_05` char(6) NOT NULL,
  PRIMARY KEY (`goodsID`),
  KEY `rm_goods_bom_goodsID_05_fk` (`goodsID_05`),
  CONSTRAINT `rm_goods_bom_goodsID_05_fk` FOREIGN KEY (`goodsID_05`) REFERENCES `bom` (`goodsID_05`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `transactions`
--

DROP TABLE IF EXISTS `transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transactions` (
  `transactionID` int NOT NULL AUTO_INCREMENT,
  `transactionTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `memberID` char(20) NOT NULL,
  `goodsID` char(10) NOT NULL,
  `transactionType` int NOT NULL,
  PRIMARY KEY (`transactionID`),
  KEY `fk_transactions_member` (`memberID`),
  KEY `fk_transactions_goods` (`goodsID`),
  CONSTRAINT `fk_transactions_goods` FOREIGN KEY (`goodsID`) REFERENCES `rm_goods` (`goodsID`),
  CONSTRAINT `fk_transactions_member` FOREIGN KEY (`memberID`) REFERENCES `group_members` (`memberID`)
) ENGINE=InnoDB AUTO_INCREMENT=3125 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;
