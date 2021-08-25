
CREATE TABLE [Admin]
( 
	[username]           varchar(100)  NOT NULL 
)
go

CREATE TABLE [City]
( 
	[postalCode]         varchar(100)  NOT NULL ,
	[name]               varchar(100)  NOT NULL ,
	[id]                 integer  IDENTITY  NOT NULL 
)
go

CREATE TABLE [Courier]
( 
	[courierUsername]    varchar(100)  NOT NULL ,
	[deliveriesCnt]      integer  NOT NULL 
	CONSTRAINT [DefaultZero_485868686]
		 DEFAULT  0
	CONSTRAINT [NotNegative_368556957]
		CHECK  ( deliveriesCnt >= 0 ),
	[profit]             decimal(10,3)  NOT NULL 
	CONSTRAINT [DefaultZero_1331925654]
		 DEFAULT  0,
	[status]             integer  NOT NULL 
	CONSTRAINT [DefaultZero_1347788965]
		 DEFAULT  0
	CONSTRAINT [ValidCourierStatus_27644847]
		CHECK  ( status BETWEEN 0 AND 1 ),
	[licensePlate]       varchar(100)  NOT NULL 
)
go

CREATE TABLE [CourierRequest]
( 
	[licensePlate]       varchar(100)  NOT NULL ,
	[username]           varchar(100)  NOT NULL 
)
go

CREATE TABLE [District]
( 
	[id]                 integer  IDENTITY  NOT NULL ,
	[name]               varchar(100)  NOT NULL ,
	[xCord]              integer  NOT NULL ,
	[yCord]              integer  NOT NULL ,
	[cityId]             integer  NOT NULL 
)
go

CREATE TABLE [Offer]
( 
	[packageId]          integer  NOT NULL ,
	[courierUsername]    varchar(100)  NOT NULL ,
	[pricePercentage]    decimal(10,3)  NOT NULL ,
	[id]                 integer  IDENTITY  NOT NULL ,
	[accepted]           integer  NOT NULL 
	CONSTRAINT [DefaultZero_1196917368]
		 DEFAULT  0
)
go

CREATE TABLE [Package]
( 
	[districtFrom]       integer  NOT NULL ,
	[districtTo]         integer  NOT NULL ,
	[type]               integer  NOT NULL 
	CONSTRAINT [ValidPackageTypes_1210137139]
		CHECK  ( type BETWEEN 0 AND 2 ),
	[weight]             decimal(10,3)  NOT NULL 
	CONSTRAINT [NotNegative_1313832326]
		CHECK  ( weight >= 0 ),
	[id]                 integer  IDENTITY  NOT NULL ,
	[status]             integer  NOT NULL 
	CONSTRAINT [DefaultZero_1430880670]
		 DEFAULT  0
	CONSTRAINT [ValidPackageStatus_348120777]
		CHECK  ( status BETWEEN 0 AND 3 ),
	[deliveryPrice]      decimal(10,3)  NULL 
	CONSTRAINT [NotNegative_1089708471]
		CHECK  ( deliveryPrice >= 0 ),
	[acceptanceTime]     datetime  NULL ,
	[senderUsername]     varchar(100)  NOT NULL ,
	[courierUsername]    varchar(100)  NULL 
)
go

CREATE TABLE [User]
( 
	[username]           varchar(100)  NOT NULL ,
	[name]               varchar(100)  NOT NULL ,
	[surname]            varchar(100)  NOT NULL ,
	[password]           varchar(100)  NOT NULL ,
	[sentPackagesCnt]    integer  NOT NULL 
	CONSTRAINT [DefaultZero_857406887]
		 DEFAULT  0
)
go

CREATE TABLE [Vehicle]
( 
	[fuelType]           integer  NOT NULL 
	CONSTRAINT [ValidFuelTypes_48148760]
		CHECK  ( fuelType BETWEEN 0 AND 2 ),
	[consumption]        decimal(10,3)  NOT NULL 
	CONSTRAINT [NotNegative_506267328]
		CHECK  ( consumption >= 0 ),
	[licensePlate]       varchar(100)  NOT NULL 
)
go

ALTER TABLE [Admin]
	ADD CONSTRAINT [XPKAdmin] PRIMARY KEY  CLUSTERED ([username] ASC)
go

ALTER TABLE [City]
	ADD CONSTRAINT [XPKCity] PRIMARY KEY  CLUSTERED ([id] ASC)
go

ALTER TABLE [City]
	ADD CONSTRAINT [XAK1City] UNIQUE ([postalCode]  ASC)
go

ALTER TABLE [City]
	ADD CONSTRAINT [XAK2City] UNIQUE ([name]  ASC)
go

ALTER TABLE [Courier]
	ADD CONSTRAINT [XPKCourier] PRIMARY KEY  CLUSTERED ([courierUsername] ASC)
go

ALTER TABLE [Courier]
	ADD CONSTRAINT [XAK1Courier] UNIQUE ([licensePlate]  ASC)
go

ALTER TABLE [CourierRequest]
	ADD CONSTRAINT [XPKCourierRequest] PRIMARY KEY  CLUSTERED ([username] ASC)
go

ALTER TABLE [District]
	ADD CONSTRAINT [XPKDistrict] PRIMARY KEY  CLUSTERED ([id] ASC)
go

ALTER TABLE [Offer]
	ADD CONSTRAINT [XPKOffer] PRIMARY KEY  CLUSTERED ([id] ASC)
go

ALTER TABLE [Package]
	ADD CONSTRAINT [XPKPackage] PRIMARY KEY  CLUSTERED ([id] ASC)
go

ALTER TABLE [User]
	ADD CONSTRAINT [XPKUser] PRIMARY KEY  CLUSTERED ([username] ASC)
go

ALTER TABLE [Vehicle]
	ADD CONSTRAINT [XPKVehicle] PRIMARY KEY  CLUSTERED ([licensePlate] ASC)
go


ALTER TABLE [Admin]
	ADD CONSTRAINT [R_2] FOREIGN KEY ([username]) REFERENCES [User]([username])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go


ALTER TABLE [Courier]
	ADD CONSTRAINT [R_3] FOREIGN KEY ([courierUsername]) REFERENCES [User]([username])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go

ALTER TABLE [Courier]
	ADD CONSTRAINT [R_9] FOREIGN KEY ([licensePlate]) REFERENCES [Vehicle]([licensePlate])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [CourierRequest]
	ADD CONSTRAINT [R_19] FOREIGN KEY ([licensePlate]) REFERENCES [Vehicle]([licensePlate])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [CourierRequest]
	ADD CONSTRAINT [R_21] FOREIGN KEY ([username]) REFERENCES [User]([username])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [District]
	ADD CONSTRAINT [R_1] FOREIGN KEY ([cityId]) REFERENCES [City]([id])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Offer]
	ADD CONSTRAINT [R_12] FOREIGN KEY ([packageId]) REFERENCES [Package]([id])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go

ALTER TABLE [Offer]
	ADD CONSTRAINT [R_13] FOREIGN KEY ([courierUsername]) REFERENCES [Courier]([courierUsername])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Package]
	ADD CONSTRAINT [R_10] FOREIGN KEY ([districtFrom]) REFERENCES [District]([id])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Package]
	ADD CONSTRAINT [R_11] FOREIGN KEY ([districtTo]) REFERENCES [District]([id])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Package]
	ADD CONSTRAINT [R_20] FOREIGN KEY ([senderUsername]) REFERENCES [User]([username])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Package]
	ADD CONSTRAINT [R_22] FOREIGN KEY ([courierUsername]) REFERENCES [Courier]([courierUsername])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go



CREATE FUNCTION getPrice
(
	-- Add the parameters for the function here
	@pkgType int, @weight float, @distance float
)
RETURNS float
AS
BEGIN
	-- Declare the return variable here
	DECLARE @ret float
	Declare @basePrice float
	Declare @weightFactor float
	Declare @weightPrice float
	-- Add the T-SQL statements to compute the return value here
	set @basePrice = CASE @pkgType   
     WHEN 0 THEN 10  
     WHEN 1 THEN 25 
     WHEN 2 THEN 75
	end

	set @weightFactor = CASE @pkgType   
     WHEN 0 THEN 0  
     WHEN 1 THEN 1
     WHEN 2 THEN 2
	end

	set @weightPrice = case @pkgType
		WHEN 0 THEN 0
		WHEN 1 THEN 100
		WHEN 2 THEN 300
	end
	
	set @ret = (@basePrice + (@weightFactor * @weight) * @weightPrice) * @distance
	-- Return the result of the function
	RETURN @ret
END

go

CREATE FUNCTION getDistance
(
	-- Add the parameters for the function here
	@districtA int, @districtB int
)
RETURNS float
AS
BEGIN
	-- Declare the return variable here
	DECLARE @ret float
	Declare @xA int, @yA int, @xB int, @yB int

	select @xA = xCord, @yA = yCord from District where id = @districtA
	select @xB = xCord, @yB = yCord from District where id = @districtB

	set @ret = SQRT(POWER(@xA - @xB, 2) + POWER(@yA - @yB, 2))
	RETURN @ret
END

go

CREATE TRIGGER [dbo].[TR_TransportOffer_Accepted]
   ON  [dbo].[Offer] 
   FOR UPDATE
AS 
BEGIN
	SET NOCOUNT ON;

    Declare @pkgId int, @offerId int, @courier varchar(100)
	Declare @cursor CURSOR
	Declare @pricePercentage float
	Declare @price float, @type int, @weight float
	Declare @d1 int, @d2 int
	Declare @distance float
	Declare @courierPrice float

	set @cursor = cursor for
	select id, packageId, courierUsername, pricePercentage
	from inserted

	open @cursor
	
	fetch next from @cursor
	into @offerId, @pkgId, @courier, @pricePercentage
	while @@FETCH_STATUS = 0
	begin
		select @type = type, @weight = weight, @d1 = districtFrom, @d2 = districtTo
		from Package where id = @pkgId

		select @distance = [dbo].[getDistance] (@d1, @d2)
		select @price = [dbo].[getPrice] (@type, @weight, @distance)

		select @courierPrice = @price * @pricePercentage / 100
		set @price = @price + @courierPrice

		update Package
		set courierUsername = @courier, 
			acceptanceTime = GETDATE(),
			status = 1,
			deliveryPrice = @price
		where id = @pkgId

		delete from Offer where packageId = @pkgId

		update Courier 
		set profit = profit + @price
		where courierUsername = @courier
		
		fetch next from @cursor
		into @offerId, @pkgId, @courier, @pricePercentage
	end
END

go

CREATE TRIGGER UpdateCourierData
   ON  Package
   FOR UPDATE
AS 
BEGIN
	SET NOCOUNT ON;

	Declare @cursorI cursor, @cursorD cursor
	Declare @pkgId int, @statusI int, @statusD int
	Declare @courier varchar(100)
	Declare @morePkgs int
	Declare @user varchar(100)

	set @cursorI = cursor for
	select id, status, courierUsername, senderUsername
	from inserted

	set @cursorD = cursor for
	select status
	from deleted

	open @cursorI
	open @cursorD

	fetch next from @cursorI
	into @pkgId, @statusI, @courier, @user

	fetch next from @cursorD
	into @statusD

	while @@FETCH_STATUS = 0
	begin
		if (@statusD = 0 and @statusI = 1)
		begin
			update [dbo].[User] set sentPackagesCnt = sentPackagesCnt + 1 where username = @user
		end

		if (@statusD = 1 and @statusI = 2)
		begin
			update Courier set status = 1 where courierUsername = @courier
		end

		if (@statusD = 2 and @statusI = 3)
		begin
			update Courier set deliveriesCnt = deliveriesCnt + 1 where courierUsername = @courier 
			select @morePkgs = count(*) from Package where courierUsername = @courier and status = 2
			if @morePkgs = 0
			begin
				update Courier set status = 0 where courierUsername = @courier
			end
		end

		fetch next from @cursorI
		into @pkgId, @statusI, @courier, @user

		fetch next from @cursorD
		into @statusD
	end
END

go

CREATE PROCEDURE SPGasBills
	@courier varchar(100),
	@d1 int,
	@d2 int
AS
BEGIN
	SET NOCOUNT ON;

	declare @fuelType int
	declare @consumption float
	declare @distance float
	declare @gasPrice float
	declare @bill float
	declare @licensePlate varchar(100)


	-- Get licensePlate
	select @licensePlate = licensePlate from Courier where courierUsername = @courier

	-- Get vehicle fuelType and consumption
	select @fuelType = fuelType, @consumption = consumption from Vehicle where licensePlate = @licensePlate

	-- get distance
	select @distance = [dbo].[getDistance] (@d1, @d2)

	-- get gas price
	select @gasPrice = case @fuelType
		when 0 then 15
		when 1 then 32
		when 2 then 36
	end

	-- calculate bill
	set @bill = @consumption * @distance * @gasPrice

	-- update profit
	update Courier set profit = profit - @bill where courierUsername = @courier
END

go

CREATE PROCEDURE SPGrantCourierRequest
	@username varchar(100), 
	@res int output
AS
BEGIN
	Declare @licensePlate varchar(100)
	set @licensePlate = coalesce((SELECT licensePlate FROM CourierRequest where username=@username), '')
	if @licensePlate = ''
		set @res = -1;
	else begin
		INSERT INTO Courier (courierUsername, licensePlate) values (@username, @licensePlate)
		if @@ROWCOUNT = 1
			DELETE FROM CourierRequest where username=@username
		select @res = count(*) from Courier where courierUsername = @username and licensePlate = @licensePlate
	end
END
