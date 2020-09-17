 create table prescriptions(
	prescription_id serial not null primary key,
	appointments_id int not null,
	medicine_name text,
	foreign key (appointments_id) references appointments(appointments_id)
);