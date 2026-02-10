DELIMITER //

DROP TRIGGER IF EXISTS tr_reservation_validate_dates//
CREATE TRIGGER tr_reservation_validate_dates
    BEFORE INSERT ON reservations
    FOR EACH ROW
BEGIN
    IF NEW.check_out_date <= NEW.check_in_date THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Check-out must be after check-in.';
END IF;
END//
DROP TRIGGER IF EXISTS tr_reservation_validate_dates_update//
CREATE TRIGGER tr_reservation_validate_dates_update
    BEFORE UPDATE ON reservations
    FOR EACH ROW
BEGIN
    IF NEW.check_out_date <= NEW.check_in_date THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Check-out must be after check-in.';
END IF;
END//

DROP TRIGGER IF EXISTS tr_reservation_room_status_insert//
CREATE TRIGGER tr_reservation_room_status_insert
    AFTER INSERT ON reservations
    FOR EACH ROW
BEGIN
    IF NEW.status IN ('PENDING', 'CONFIRMED') THEN
    UPDATE rooms SET roomStatus = 'RESERVED' WHERE roomId = NEW.roomId;
END IF;
END//

DROP TRIGGER IF EXISTS tr_reservation_room_status_update//
CREATE TRIGGER tr_reservation_room_status_update
    AFTER UPDATE ON reservations
    FOR EACH ROW
BEGIN
    IF NEW.status = 'CHECKED_IN' THEN
    UPDATE rooms SET roomStatus = 'OCCUPIED' WHERE roomId = NEW.roomId;
    ELSEIF NEW.status IN ('CHECKED_OUT', 'CANCELLED') THEN
    UPDATE rooms SET roomStatus = 'AVAILABLE' WHERE roomId = NEW.roomId;
END IF;
END//

DROP TRIGGER IF EXISTS tr_bill_update_reservation_total//
CREATE TRIGGER tr_bill_update_reservation_total
    AFTER INSERT ON bills
    FOR EACH ROW
BEGIN
    UPDATE reservations SET total_amount = NEW.total_amount WHERE reservationId = NEW.reservationId;
END//

DELIMITER ;