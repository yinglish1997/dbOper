# dbOper
数据库操作：把之前计算出来的那些统计值都放到对应的数据库中，日均分，日评论量，日情感分放在mysql，关注点放在mongodb。
SplitSqlRecords有别于SplitMovieFile，不是切分csv记录，而是spark连接数据库，处理数据（以id为键分类）后分别保存为txt文件。
两个类的构造函数参数都是文件txt的地址所在，mysqlOper调用CommentStatistic和EmotionAnalysis计算以得值，mongoOper调用FouceAnalysis计算关注点，都是向远程数据库插入数据。

