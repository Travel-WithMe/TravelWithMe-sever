set SQL_SAFE_UPDATES = 0;

delete from member_interest;
delete from interest;

insert into interest (id, type)
values (1, '하이킹'),(2, '서핑'),(3, '다이빙'),(4, '스노클링'),(5, '사파리'),(6, '스키'),(7, '자전거'),
(8, '액티비티'),(9, '음식 체험'),(10, '음악 감상'),(11, '공연 감상'),(12, '전시회'),(13, '예술 관람'),
(14, '사진 촬영'),(15, '지역 축제'),(16, '계획형'),(17, '즉흥형');

set SQL_SAFE_UPDATES = 0;
