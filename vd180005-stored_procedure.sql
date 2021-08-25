

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
