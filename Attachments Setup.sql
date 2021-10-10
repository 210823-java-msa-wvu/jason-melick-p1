create table project1.attachments(

att_id				serial				primary key,
att_by				integer				not null		
	constraint fk_emp		
		references employees(emp_id)
					on delete cascade,

file_name			text				not null,
file_ext			varchar				not null,
file				bytea				not null,

att_date			timestamp			not null								default current_timestamp

);

alter table project1.attachments drop constraint fk_emp;

alter table project1.attachments 
		add foreign key(att_by)
			references project1.employees(emp_id);