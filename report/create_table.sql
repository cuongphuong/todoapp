create table tb_work (
    work_id int not null auto_increment,
    work_name varchar(50),
    start_date datetime, 
    end_date datetime,
    status int,
    delete_flg int,
    primary key (work_id)
);