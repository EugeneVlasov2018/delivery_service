create table if not exists client
(
	id serial not null
		constraint client_pk
			primary key,
	name varchar(100),
	address varchar(250),
	phone_number varchar(50)
);

alter table client owner to postgres;

create table if not exists vehicle_crew
(
	id serial not null
		constraint vehicle_crew_pk
			primary key,
	crew_status varchar(50) default 'READY_FOR_RIDE'::character varying not null
);

alter table vehicle_crew owner to postgres;

create table if not exists courier
(
	id serial not null
		constraint courier_pk
			primary key,
	firstname_lastname varchar(50),
	phone_number varchar(50) not null,
	crew_id integer
		constraint courier_vehicle_crew_id_fk
			references vehicle_crew
);

alter table courier owner to postgres;

create table if not exists transport
(
	id serial not null
		constraint transport_pk
			primary key,
	registration_number varchar(50) not null,
	crew_id integer
		constraint transport_vehicle_crew_id_fk
			references vehicle_crew,
	transport_status varchar(50) default 'FREE'::character varying
);

alter table transport owner to postgres;

create table if not exists route
(
	id serial not null
		constraint route_pk
			primary key,
	start_route timestamp,
	end_route timestamp,
	transport_id integer
		constraint route_transport_id_fk
			references transport,
	route_status varchar(50) default 'FUTURE_ROUTE'::character varying
);

alter table route owner to postgres;

create table if not exists route_client
(
	route_id integer not null
		constraint route_client_route_id_fk
			references route,
	client_id integer not null
		constraint route_client_client_id_fk
			references client
);

alter table route_client owner to postgres;

