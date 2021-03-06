  CREATE TABLE "QB_CATEGORIES" 
   (	"CATEGORY_ID" NUMBER(3,0), 
	"CATEGORY_NAME" VARCHAR2(255 BYTE), 
	"ADDED_BY" VARCHAR2(255 BYTE), 
	"MARK_TO_DELETE" NUMBER(1,0), 
	 CONSTRAINT "QB_CATEGORIES_PK" PRIMARY KEY ("CATEGORY_ID")
   );
   insert into qb_categories(category_id,mark_to_delete) values(0,1);
   CREATE TABLE "QB_DIFFICULTY_LEVEL" 
   (	"DIFFICULTY_LEVEL_ID" NUMBER(1,0), 
	"DIFFICULTY_LEVEL_NAME" VARCHAR2(30 BYTE), 
	 CONSTRAINT "QB_DIFFICULTY_LEVEL_PK" PRIMARY KEY ("DIFFICULTY_LEVEL_ID")
   );
   insert into QB_DIFFICULTY_LEVEL(DIFFICULTY_LEVEL_ID,DIFFICULTY_LEVEL_NAME) values(1,'EASY');
    insert into QB_DIFFICULTY_LEVEL(DIFFICULTY_LEVEL_ID,DIFFICULTY_LEVEL_NAME) values(2,'FAIR');
     insert into QB_DIFFICULTY_LEVEL(DIFFICULTY_LEVEL_ID,DIFFICULTY_LEVEL_NAME) values(3,'MEDIUM');
      insert into QB_DIFFICULTY_LEVEL(DIFFICULTY_LEVEL_ID,DIFFICULTY_LEVEL_NAME) values(4,'HARD');
       insert into QB_DIFFICULTY_LEVEL(DIFFICULTY_LEVEL_ID,DIFFICULTY_LEVEL_NAME) values(5,'VERY HARD');

  CREATE TABLE "QB_PAPER" 
   (	"PAPER_ID" NUMBER(10,0), 
	"QUESTION_LIST" VARCHAR2(1000 BYTE), 
	"NO_OF_QUESTIONS" NUMBER(10,0), 
	"TARGET_COLLEGE" VARCHAR2(100 BYTE), 
	"COURSE" VARCHAR2(100 BYTE), 
	"CREATED_DATE" DATE, 
	"EXAM_DATE" DATE, 
	"CREATED_BY" VARCHAR2(255 BYTE), 
	"CREATED_PURPOSE" VARCHAR2(255 BYTE), 
	"MARK_TO_DELETE" NUMBER(1,0), 
	"PAPER_NAME" VARCHAR2(255 BYTE), 
	 PRIMARY KEY ("PAPER_ID")
   );
INSERT INTO QB_PAPER("PAPER_ID",mark_to_delete) VALUES(0,1);
CREATE TABLE "QB_SUBCATEGORIES" 
   (	"SUBCATEGORY_ID" NUMBER(6,0), 
	"PARENT_ID" NUMBER(3,0), 
	"CATEGORY_NAME" VARCHAR2(255 BYTE), 
	"ADDED_BY" VARCHAR2(255 BYTE), 
	"MARK_TO_DELETE" NUMBER(1,0), 
	 CONSTRAINT "QB_SUBCATEGORIES_PK" PRIMARY KEY ("SUBCATEGORY_ID")
   );
    insert into qb_SUBcategories(SUBcategory_id,mark_to_delete) values(0,1);
    CREATE TABLE "QB_TYPE" 
   (	"TYPE_ID" NUMBER(1,0), 
	"TYPE_NAME" VARCHAR2(30 BYTE)
   );
   INSERT INTO QB_TYPE VALUES(1,'OBJECTIVE');
    INSERT INTO QB_TYPE VALUES(2,'SUBJECTIVE');
     INSERT INTO QB_TYPE VALUES(3,'TRUE/FALSE');
     
  CREATE TABLE "QB_USER_PROFILES" 
   (	"USERNAME" VARCHAR2(255 BYTE), 
	"ROLE" VARCHAR2(30 BYTE), 
	"CATEGORY_ASSIGNED" NUMBER(3,0), 
	"MARK_TO_DELETE" NUMBER(1,0), 
	"PASSWORD" VARCHAR2(255 BYTE), 
	 CONSTRAINT "QB_USER_PROFILES_PK" PRIMARY KEY ("USERNAME")
   );

  CREATE TABLE "QUESTION_BANK" 
   (	"Q_ID" NUMBER(10,0), 
	"QUESTION_TEXT" VARCHAR2(255 BYTE), 
	"CATEGORY_ID" NUMBER(3,0), 
	"SUBCATEGORY_ID" NUMBER(6,0), 
	"IMAGE" BLOB, 
	"TYPE" NUMBER(1,0), 
	"DIFFICULTY" NUMBER(1,0), 
	"MARKS" NUMBER(2,0), 
	"ADDED_BY" VARCHAR2(255 BYTE), 
	"CHOICE1" VARCHAR2(255 BYTE), 
	"CHOICE2" VARCHAR2(255 BYTE), 
	"CHOICE3" VARCHAR2(255 BYTE), 
	"CHOICE4" VARCHAR2(255 BYTE), 
	"CORRECT_ANSWER" NUMBER(2,0), 
	"MARK_TO_DELETE" NUMBER(1,0), 
	"TIME_ASSIGNED_IN_MINUTES" NUMBER(4,0), 
	 CONSTRAINT "QB_PK" PRIMARY KEY ("Q_ID")
   );
   INSERT INTO QUESTION_BANK(Q_ID,mark_to_delete) VALUES (0,1);
   CREATE TABLE "QUESTION_BANK_EDIT_HISTORY" 
   (	"Q_ID" NUMBER(10,0), 
	"QUESTION_TEXT" VARCHAR2(255 BYTE), 
	"CATEGORY_ID" NUMBER(3,0), 
	"SUBCATEGORY_ID" NUMBER(6,0), 
	"IMAGE" BLOB, 
	"TYPE" NUMBER(1,0), 
	"DIFFICULTY" NUMBER(1,0), 
	"MARKS" NUMBER(2,0), 
	"ADDED_BY" VARCHAR2(255 BYTE), 
	"CHOICE1" VARCHAR2(255 BYTE), 
	"CHOICE2" VARCHAR2(255 BYTE), 
	"CHOICE3" VARCHAR2(255 BYTE), 
	"CHOICE4" VARCHAR2(255 BYTE), 
	"CORRECT_ANSWER" NUMBER(2,0), 
	"MARK_TO_DELETE" NUMBER(1,0), 
	"TIME_ASSIGNED_IN_MINUTES" NUMBER(4,0), 
	 CONSTRAINT "QB_EDITED_HISTORY_PK" PRIMARY KEY ("Q_ID")
   );
   INSERT INTO QUESTION_BANK_EDIT_HISTORY(Q_ID,mark_to_delete) VALUES (0,1);
   Alter table qb_categories   ADD CONSTRAINT "QB_CATEGORIES_FK1" FOREIGN KEY ("ADDED_BY") 
	  REFERENCES "QB_USER_PROFILES" ("USERNAME");
 ALTER table QUESTION_BANK ADD CONSTRAINT "QB_FK1" FOREIGN KEY ("CATEGORY_ID") REFERENCES "QB_CATEGORIES" ("CATEGORY_ID") ;
	 	  ALTER table QUESTION_BANK ADD CONSTRAINT "QB_FK2" FOREIGN KEY ("SUBCATEGORY_ID") REFERENCES "QB_SUBCATEGORIES" ("SUBCATEGORY_ID") ;
		  ALTER table QUESTION_BANK ADD CONSTRAINT  "QB_QUESTION_BANK_FK4" FOREIGN KEY ("DIFFICULTY") REFERENCES "QB_DIFFICULTY_LEVEL" ("DIFFICULTY_LEVEL_ID") ;