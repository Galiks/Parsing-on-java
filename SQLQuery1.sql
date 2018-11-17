USE master 
GO 

IF EXISTS(SELECT * FROM sys.databases WHERE name='Parsing') 
BEGIN 
DROP DATABASE Parsing
END 
GO 

CREATE DATABASE Parsing
GO 

USE Parsing
GO

CREATE TABLE [Cashback] ( 
	[id] int IDENTITY(1,1) NOT NULL CONSTRAINT [PK_Cashback] primary key, 
	Cashback float NOT NULL,
	ShopName varchar(100) NOT NULL,
);