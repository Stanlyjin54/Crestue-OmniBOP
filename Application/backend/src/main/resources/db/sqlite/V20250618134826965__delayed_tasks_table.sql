drop table if exists `delayed_tasks`;
create table `delayed_tasks` (
	id	INTEGER PRIMARY KEY AUTOINCREMENT,
	task_type	VARCHAR(512)	not null,
	task_data	VARCHAR(512)	not null,
	status	VARCHAR(512)	not null,
	execute_time	DATETIME	not null,
	create_time	DATETIME	not null,
	update_time	DATETIME	not null
);