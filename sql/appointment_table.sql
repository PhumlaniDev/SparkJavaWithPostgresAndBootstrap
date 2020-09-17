create table appointments (
	appointments_id serial not null primary key,
	patients_id int not null,
	appointment_date date not null default current_date,
	foreign key (patients_id) references patients(patients_id)
);

select * from appointments;