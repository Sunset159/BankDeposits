DELETE FROM deposits;
DELETE FROM clients;
DELETE FROM banks;

INSERT INTO clients (name, short_name, address, legal_form) VALUES
('ООО Ромашка', 'Ромашка', 'г. Москва, ул. Ленина, д.1', 'ООО'),
('АО Техно', 'Техно', 'г. Санкт-Петербург, Невский проспект, д.10', 'АО'),
('ИП Иванов', 'Иванов', 'г. Казань, ул. Баумана, д.5', 'ИП');

INSERT INTO banks (name, bik) VALUES
('Банк Первый', '044525225'),
('Финанс Банк', '044525974');

INSERT INTO deposits (client_id, bank_id, open_date, percent, term_months) VALUES
(1, 1, '2024-01-15', 5.5, 12),
(2, 1, '2024-02-01', 6.0, 24),
(3, 2, '2024-03-10', 4.8, 6);