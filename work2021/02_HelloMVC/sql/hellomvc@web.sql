--==========================================
-- 관리자(system) 계정
--==========================================
--web계정 생성
create user web
identified by web
default tablespace users;

grant connect, resource to web;


--==========================================
-- web 계정
--==========================================
create table member (
    member_id varchar2(15),                     --PK
    password varchar2(300) not null,            --필수
    member_name varchar2(30) not null,          --필수
    member_role char(1) default 'U' not null,   --필수
    gender char(1),
    birthday date,
    email varchar2(100),
    phone char(11) not null,                    --필수
    address varchar2(200),
    hobby varchar2(100),
    enroll_date date default sysdate,
    constraint pk_member_id primary key(member_id),
    constraint ck_gender check(gender in ('M', 'F')),
    constraint ck_member_role check(member_role in ('U', 'A'))
);

--회원 추가
insert into member
values (
    'honggd', '1234', '홍길동', 'U', 'M', to_date('20000909','yyyymmdd'),
    'honggd@naver.com', '01012341234', '서울시 강남구', '운동,등산,독서', default
);

insert into member
values (
    'qwerty', '1234', '쿠어티', 'U', 'F', to_date('19900215','yyyymmdd'),
    'qwerty@naver.com', '01012341234', '서울시 송파구', '운동,등산', default
);

insert into member
values (
    'admin', '1234', '관리자', 'A', 'M', to_date('19801010','yyyymmdd'),
    'admin@naver.com', '01056785678', '서울시 관악구', '게임,독서', default
);

select * from member;
commit;

desc member;

update member 
set password = '1ARVn2Auq2/WAqx2gNrL+q3RNjAzXpUfCXrzkA6d4Xa22yhRLy4AC50E+6UTPoscbo31nbOoq51gvkuXzJ6B2w==';

commit;
--1ARVn2Auq2/WAqx2gNrL+q3RNjAzXpUfCXrzkA6d4Xa22yhRLy4AC50E+6UTPoscbo31nbOoq51gvkuXzJ6B2w==
--Dm6EiH9LY3KEe1YC8zINomgxhqUBPWHJzAxT5SqxoBvEWhVg+hyrsI08vt8SvDc+hWeOxptlgTyi48iGjE8INA==


--페이징
--rownum
select *
from (
        select rownum rnum, E.*
        from (
                select *
                from member
                order by enroll_date desc
            ) E
        ) E
where rnum between 11 and 20;

--row_number
select *
from (
        select M.*,
               row_number() over(order by enroll_date desc) rnum
        from member M
    ) M
where rnum between 11 and 20;

--select * from ( select M.*, row_number() over(order by enroll_date desc) rnum from member M ) M where rnum between 11 and 20

--페이징 검색
select *
from (
        select M.*,
               row_number() over(order by enroll_date desc) rnum
        from member M
        where member_id like '%a%'
    )
where rnum between 11 and 20;

--select * from ( select M.*, row_number() over(order by enroll_date desc) rnum from member M where # like ? ) where rnum between ? and ?

--------------------------------------------
-- 게시판
--------------------------------------------

--게시판생성
CREATE TABLE BOARD (
    BOARD_NO NUMBER,
    BOARD_TITLE VARCHAR2(50) NOT NULL,
    BOARD_WRITER VARCHAR2(15),
    BOARD_CONTENT VARCHAR2(2000) NOT NULL,
    BOARD_ORIGINAL_FILENAME VARCHAR2(200),
    BOARD_RENAMED_FILENAME VARCHAR2(200),
    BOARD_DATE DATE DEFAULT SYSDATE,
    BOARD_READ_COUNT NUMBER DEFAULT 0,
    CONSTRAINT PK_BOARD_NO PRIMARY KEY(BOARD_NO),
    CONSTRAINT FK_BOARD_WRITER FOREIGN KEY(BOARD_WRITER)
                                         REFERENCES MEMBER(MEMBER_ID)
                                         ON DELETE SET NULL
);

COMMENT ON TABLE BOARD IS '게시판테이블';
COMMENT ON COLUMN "BOARD"."BOARD_NO" IS '게시글번호';
COMMENT ON COLUMN "BOARD"."BOARD_TITLE" IS '게시글제목';
COMMENT ON COLUMN "BOARD"."BOARD_WRITER" IS '게시글작성자 아이디';
COMMENT ON COLUMN "BOARD"."BOARD_CONTENT" IS '게시글내용';
COMMENT ON COLUMN "BOARD"."BOARD_ORIGINAL_FILENAME" IS '첨부파일원래이름';
COMMENT ON COLUMN "BOARD"."BOARD_RENAMED_FILENAME" IS '첨부파일변경이름';
COMMENT ON COLUMN "BOARD"."BOARD_DATE" IS '게시글올린날짜';
COMMENT ON COLUMN "BOARD"."BOARD_READ_COUNT" IS '조회수';

--게시판시퀀스생성
CREATE SEQUENCE SEQ_BOARD_NO
START WITH 1
INCREMENT BY 1
NOMINVALUE
NOMAXVALUE
NOCYCLE
NOCACHE;
	
    
select * from board
order by board_no desc;
commit;


--select * 
--from( 
--    select row_number() over(order by board_no desc) rnum, 
--           b.* 
--    from board b
--    ) v 
--where rnum between ? and ?

--마지막 등록된 게시글 번호 가져오기
select seq_board_no.currval 
from dual;

--------------------------------------------------
--댓글기능 구현
--------------------------------------------------
--게시판(board) ---- fk ---> 게시판댓글(board_comment) : 댓글/답글
--댓글을 특정게시글(fk)을 참조
--답글을 특정게시글(fk)과 특정댓글(fk)을 참조

create table board_comment (
    board_comment_no number,                    -- pk 고유번호
    board_comment_level number default 1,       -- 댓글 1, 대댓글 2
    board_comment_writer varchar2(15),          -- member.member_id fk
    board_comment_content varchar2(2000),
    
    board_ref number,                           -- board.board_no fk
    board_comment_ref number,                   -- board_comment.board_comment_no  댓글일때 null, 대댓글때 board_comment_no참조
    board_comment_date date default sysdate,
    
    constraint pk_board_comment_no primary key(board_comment_no),
    constraint fk_board_comment_writer foreign key(board_comment_writer)
                                                       references member(member_id)
                                                       on delete set null,
    constraint fk_board_ref foreign key (board_ref)
                                     references board(board_no)
                                     on delete cascade,
    constraint fk_board_comment_ref foreign key (board_comment_ref)
                                                   references board_comment (board_comment_no)
                                                   on delete cascade
);


--drop table board_comment;

comment on table board_comment is '게시판댓글테이블';

comment on column board_comment.board_comment_no is '게시판댓글번호';
comment on column board_comment.board_comment_level is '게시판댓글 레벨: 1-댓글(기본값), 2-대댓글';
comment on column board_comment.board_comment_writer is '게시판댓글 작성자';
comment on column board_comment.board_comment_content is '게시판댓글';
comment on column board_comment.board_ref is '참조원글번호';
comment on column board_comment.board_comment_ref is '게시판댓글 참조번호';
comment on column board_comment.board_comment_date is '게시판댓글 작성일';

--시퀀스 생성
create sequence seq_board_comment_no
start with 1
increment by 1
nominvalue
nomaxvalue
nocycle
nocache;

--게시글 - 댓글 - 답글
select *
from board
order by board_no desc;

--68번 게시글 댓글 
select * from board_comment;
insert into board_comment
values(
    seq_board_comment_no.nextval,
    default, --level
    'admin', --작성자
    '수고하셨습니다.',
    68, --게시글 번호
    null,
    default --작성일
);

insert into board_comment
values(
    seq_board_comment_no.nextval,
    default, --level
    'abcd', --작성자
    '좋은 하루 보내세요~',
    68, --게시글 번호
    null,
    default --작성일
);
insert into board_comment
values(
    seq_board_comment_no.nextval,
    default, --level
    'honggd', --작성자
    '불금은 빡세게~',
    68, --게시글 번호
    null,
    default --작성일
);

--68번게시글 3번 댓글에 대한 대댓글(답글)
insert into board_comment
values(
    seq_board_comment_no.nextval,
    2, --level
    'wxyz', --작성자
    '저는 오늘 연차입니다.',
    68, --게시글 번호
    3,  -- 참조하는 댓글 번호
    default --작성일
);
insert into board_comment
values(
    seq_board_comment_no.nextval,
    2, --level
    'bear', --작성자
    '저는 월요일 연차입니다.',
    68, --게시글 번호
    3,  -- 참조하는 댓글 번호
    default --작성일
);
insert into board_comment
values(
    seq_board_comment_no.nextval,
    2, --level
    'tetete', --작성자
    '읽어주셔서 감사합니다.',
    68, --게시글 번호
    2,  -- 참조하는 댓글 번호
    default --작성일
);
select * from board_comment;

--계층형쿼리
--행과 행간의 부모자식 맺어서 결과집합을 리턴
--start with : 부모행의 조건절
--connect by : 부모행(부모컬럼 앞에 prior키워드 작성)과 자식행의 관계 조건절

select lpad(' ',(level - 1) * 5) || board_comment_content, 
       level,
       BC.*
from board_comment BC
where board_ref = 68
start with board_comment_level = 1 -- 댓글
connect by board_comment_ref = prior board_comment_no
order siblings by board_comment_no;

--kh계정 : 계층형 쿼리
--employee테이블 조직도를 조회
--manager_id ---> emp_id
--부모행은 n행이상일 수 있다.
select * from employee;

select lpad(' ', (level - 1) * 5) || emp_name 조직도
from employee
where quit_yn = 'N'
start with job_code = 'J1'
connect by manager_id = prior emp_id;



























