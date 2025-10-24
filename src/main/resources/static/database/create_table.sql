create table allowance
(
    use_yn           bit default b'1' null,
    allowance_amount bigint           null,
    allowance_id     bigint auto_increment
        primary key,
    created_at       datetime(6)      null,
    payment_id       bigint           null,
    updated_at       datetime(6)      null,
    allowance_name   varchar(255)     null,
    modified_by      varchar(255)     null
);

create table approval
(
    approval_form_id int               not null,
    department_id    int               not null,
    status           char default 'w'  not null,
    use_yn           bit  default b'1' null,
    approval_id      bigint auto_increment
        primary key,
    created_at       datetime(6)       null,
    updated_at       datetime(6)       null,
    approval_content longtext          null,
    modified_by      varchar(255)      null,
    username         varchar(255)      not null
);

create table approval_file
(
    use_yn      bit default b'1' null,
    approval_id bigint           not null,
    created_at  datetime(6)      null,
    file_id     bigint auto_increment
        primary key,
    updated_at  datetime(6)      null,
    extension   varchar(255)     not null,
    modified_by varchar(255)     null,
    ori_name    varchar(255)     not null,
    save_name   varchar(255)     not null
);

create table approval_form
(
    approval_form_id int              not null
        primary key,
    use_yn           bit default b'1' null,
    created_at       datetime(6)      null,
    updated_at       datetime(6)      null,
    form_content     longtext         null,
    form_title       varchar(255)     not null,
    modified_by      varchar(255)     null
);

create table approver
(
    approval_order int               not null,
    approve_yn     char default 'w'  not null,
    use_yn         bit  default b'1' null,
    approval_id    bigint            not null,
    approver_id    bigint auto_increment
        primary key,
    created_at     datetime(6)       null,
    updated_at     datetime(6)       null,
    modified_by    varchar(255)      null,
    username       varchar(255)      not null
);

create table attendance
(
    is_holiday          char             null,
    use_yn              bit default b'1' null,
    attendance_id       bigint auto_increment
        primary key,
    check_in_date_time  datetime(6)      null,
    check_out_date_time datetime(6)      null,
    created_at          datetime(6)      null,
    updated_at          datetime(6)      null,
    modified_by         varchar(255)     null,
    username            varchar(255)     not null,
    work_status         varchar(255)     not null
);

create table board
(
    department_id int                 null,
    use_yn        bit    default b'1' null,
    board_id      bigint auto_increment
        primary key,
    created_at    datetime(6)         null,
    updated_at    datetime(6)         null,
    view_count    bigint default 0    null,
    content       text                null,
    modified_by   varchar(255)        null,
    title         varchar(255)        not null,
    username      varchar(255)        not null
);

create table board_file
(
    use_yn      bit default b'1' null,
    board_id    bigint           not null,
    created_at  datetime(6)      null,
    file_id     bigint auto_increment
        primary key,
    updated_at  datetime(6)      null,
    extension   varchar(255)     not null,
    modified_by varchar(255)     null,
    ori_name    varchar(255)     not null,
    save_name   varchar(255)     not null
);

create table chat_message
(
    use_yn            bit default b'1' null,
    chat_room_id      bigint           null,
    created_at        datetime(6)      null,
    message_id        bigint auto_increment
        primary key,
    sent_at           datetime(6)      null,
    updated_at        datetime(6)      null,
    employee_username varchar(255)     null,
    message_content   varchar(255)     null,
    message_type      varchar(255)     null,
    modified_by       varchar(255)     null
);

create table chat_participant
(
    use_yn                bit default b'1' null,
    chat_room_id          bigint           null,
    created_at            datetime(6)      null,
    last_check_message_id bigint           null,
    participant_id        bigint auto_increment
        primary key,
    updated_at            datetime(6)      null,
    employee_username     varchar(255)     null,
    modified_by           varchar(255)     null
);

create table chat_room
(
    use_yn          bit default b'1' null,
    chat_room_id    bigint auto_increment
        primary key,
    created_at      datetime(6)      null,
    updated_at      datetime(6)      null,
    chat_room_title varchar(255)     null,
    chat_room_type  varchar(255)     null,
    modified_by     varchar(255)     null
);

create table deduction
(
    use_yn           bit default b'1' null,
    created_at       datetime(6)      null,
    deduction_amount bigint           null,
    deduction_id     bigint auto_increment
        primary key,
    payment_id       bigint           null,
    updated_at       datetime(6)      null,
    deduction_name   varchar(255)     null,
    modified_by      varchar(255)     null
);

create table department
(
    department_id        int              auto_increment
        primary key,
    parent_department_id int              null,
    use_yn               bit default b'1' null,
    created_at           datetime(6)      null,
    updated_at           datetime(6)      null,
    department_name      varchar(255)     not null,
    modified_by          varchar(255)     null
);

create table employee
(
    account_non_expired     bit    default b'1' null,
    account_non_locked      bit    default b'1' null,
    birth_date              date                null,
    credentials_non_expired bit    default b'1' null,
    department_id           int                 null,
    enabled                 bit    default b'1' null,
    gender                  char                null,
    hire_date               date                null,
    last_working_day        date                null,
    position_id             int                 null,
    role_id                 int    default 2    null,
    use_yn                  bit    default b'1' null,
    current_base_salary     bigint default 0    null,
    username                varchar(20)         not null
        primary key,
    address                 varchar(255)        null,
    direct_phone            varchar(255)        null,
    employee_type           varchar(255)        null,
    english_name            varchar(255)        null,
    external_email          varchar(255)        null,
    mobile_phone            varchar(255)        null,
    name                    varchar(255)        null,
    nationality             varchar(255)        null,
    password                varchar(255)        not null,
    resident_number         varchar(255)        null,
    responsibility          varchar(255)        null,
    visa_status             varchar(255)        null,
    encoded_email_password  varbinary(255)      null,
    updated_at  datetime(6)      null,
    created_at  datetime(6)      null,
    modified_by varchar(255)     null
    constraint UK76jarv83fdnjla445iwiq23nh
        unique (mobile_phone)
);

create table employee_file
(
    use_yn      bit default b'1' null,
    created_at  datetime(6)      null,
    file_id     bigint auto_increment
        primary key,
    updated_at  datetime(6)      null,
    extension   varchar(255)     not null,
    modified_by varchar(255)     null,
    ori_name    varchar(255)     not null,
    save_name   varchar(255)     not null,
    username    varchar(255)     not null
);

create table holiday
(
    is_holiday  char             null,
    locdate     date             null,
    use_yn      bit default b'1' null,
    created_at  datetime(6)      null,
    holiday_id  bigint auto_increment
        primary key,
    updated_at  datetime(6)      null,
    date_name   varchar(255)     null,
    modified_by varchar(255)     null
);

create table message_file
(
    use_yn      bit default b'1' null,
    created_at  datetime(6)      null,
    file_id     bigint auto_increment
        primary key,
    message_id  bigint           not null,
    updated_at  datetime(6)      null,
    extension   varchar(255)     not null,
    modified_by varchar(255)     null,
    ori_name    varchar(255)     not null,
    save_name   varchar(255)     not null
);

create table personal_schedule
(
    use_yn               bit default b'1' null,
    created_at           datetime(6)      null,
    personal_schedule_id bigint auto_increment
        primary key,
    schedule_date_time   datetime(6)      not null,
    updated_at           datetime(6)      null,
    address              varchar(255)     null,
    content              text             null,
    modified_by          varchar(255)     null,
    schedule_name        varchar(255)     not null,
    username             varchar(255)     not null
);

create table `position`
(
    parent_position_id int              null,
    position_id        int auto_increment
        primary key,
    use_yn             bit default b'1' null,
    created_at         datetime(6)      null,
    updated_at         datetime(6)      null,
    modified_by        varchar(255)     null,
    position_name      varchar(255)     not null
);

create table refresh_token
(
    token_id bigint auto_increment
        primary key,
    body     varchar(255) null,
    username varchar(255) null
);

create table role
(
    role_id   int          not null
        primary key,
    role_name varchar(255) null
);

create table salary_payment
(
    use_yn       bit default b'1' null,
    base_salary  bigint           null,
    created_at   datetime(6)      null,
    payment_date datetime(6)      null,
    payment_id   bigint auto_increment
        primary key,
    updated_at   datetime(6)      null,
    modified_by  varchar(255)     null,
    username     varchar(255)     null
);

create table vacation
(
    remaining_vacation int default 0    null,
    total_vacation     int default 0    null,
    use_yn             bit default b'1' null,
    used_vacation      int default 0    null,
    vacation_id        int auto_increment
        primary key,
    created_at         datetime(6)      null,
    updated_at         datetime(6)      null,
    modified_by        varchar(255)     null,
    username           varchar(255)     not null
);

create table vacation_detail
(
    end_date           date             not null,
    start_date         date             not null,
    use_yn             bit default b'1' null,
    used_days          int              null,
    vacation_id        int              not null,
    vacation_type_id   int              not null,
    approval_id        bigint           not null,
    created_at         datetime(6)      null,
    updated_at         datetime(6)      null,
    vacation_detail_id bigint auto_increment
        primary key,
    modified_by        varchar(255)     null
);

create table vacation_type
(
    use_yn             bit default b'1' null,
    vacation_type_id   int              not null
        primary key,
    created_at         datetime(6)      null,
    updated_at         datetime(6)      null,
    modified_by        varchar(255)     null,
    vacation_type_name varchar(255)     null
);

CREATE TABLE `notification` (
  `notification_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `modified_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `use_yn` bit(1) DEFAULT b'1',
  `is_read` bit(1) NOT NULL,
  `notification_type` varchar(255) NOT NULL,
  `related_id` bigint(20) NOT NULL,
  `username` varchar(255) NOT NULL,
  `content` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`notification_id`)
);



