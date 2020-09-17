 create table prescriptions(
	prescription_id serial not null primary key,
	doctors_id int not null,
	medicine_name text,
	doctors_name text,
	foreign key (doctors_id) references doctors(doctors_id)
);