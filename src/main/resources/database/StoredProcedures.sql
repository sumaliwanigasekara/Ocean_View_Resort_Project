
DELIMITER //
DROP FUNCTION IF EXISTS fn_room_charge//
CREATE FUNCTION fn_room_charge(rate_per_night DECIMAL(10,2), check_in DATE, check_out DATE)
    RETURNS DECIMAL(10,2)
    DETERMINISTIC
BEGIN
    DECLARE nights INT;
    SET nights = DATEDIFF(check_out, check_in);
RETURN rate_per_night * nights;
END//

DROP PROCEDURE IF EXISTS sp_generate_bill//
CREATE PROCEDURE sp_generate_bill(
    IN p_reservation_id BIGINT,
    IN p_service_charges DECIMAL(10,2),
    IN p_discount_amount DECIMAL(10,2),
    OUT p_bill_id BIGINT
)
BEGIN
    DECLARE v_rate DECIMAL(10,2);
    DECLARE v_check_in DATE;
    DECLARE v_check_out DATE;
    DECLARE v_room_total DECIMAL(10,2);
    DECLARE v_tax DECIMAL(10,2);

SELECT r.ratePerNight, res.check_in_date, res.check_out_date
INTO v_rate, v_check_in, v_check_out
FROM reservations res
         JOIN rooms r ON res.roomId = r.roomId
WHERE res.reservationId = p_reservation_id;

SET v_room_total = fn_room_charge(v_rate, v_check_in, v_check_out);
    SET v_tax = ROUND(v_room_total * 0.10, 2);

INSERT INTO bills (reservationId, room_charges, service_charges, tax_amount, discount_amount, total_amount, status)
VALUES (p_reservation_id, v_room_total, p_service_charges, v_tax, p_discount_amount,
        v_room_total + p_service_charges + v_tax - p_discount_amount, 'PENDING');

SET p_bill_id = LAST_INSERT_ID();
END//

DELIMITER ;