
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