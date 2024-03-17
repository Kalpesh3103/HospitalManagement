drop database if exists doctorManagement;
create database doctorManagement;

use doctorManagement;

CREATE TABLE doctor (
    doctor_id int,
    address varchar(200),
    doctor_name varchar(200)
);

insert into doctor values(1, '305 W 46th St, New York, NY 10036', 'Dr. Andreas Johansson');
insert into doctor values(2, '20 W 29th St, New York, NY 10001', 'Dr. Leszek Wlodarczak');
insert into doctor values(3, '69 W 38th St, New York, NY 10018', 'Dr. Dawid Malecki');
insert into doctor values(4, '350 W 48th St, New York, NY 10018', 'Dr. Johny Lundgren');
insert into doctor values(5, '1335 6th Ave, New York, NY 10019', 'Dr. Tonny');