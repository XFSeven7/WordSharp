# WordSharp
Hi there! 喜闻乐见

## 起因

做这个破玩意儿的起因：[我的英语糟透了，于是我做了这件事](https://www.jianshu.com/p/b825940505ac)
## 简述
这是一款功能简单的统计工具，感觉叫工具都是在抬举这些代码，这些代码可以干下面这些事情。

统计指定格式的字幕文件，这些字幕内容需要长这种形状才可以被统计。

```
4
00:00:19,930 --> 00:00:21,560
Happy Birthday, sweetie.

5
00:00:21,570 --> 00:00:23,410
I love you, baby girl.

6
00:00:23,410 --> 00:00:25,570
You're the best.

7
00:00:25,570 --> 00:00:28,100
I'm just here for the cake.

8
00:00:29,510 --> 00:00:33,510
Well, four out of the five Duncans are excited.
```

我的英文字幕都是在[这个](http://subhd.com/)网站下载的，但这也不代表这个网站的所有字幕都是上述格式。

可以通过代码得到简化的字幕，长下面这样：

```
i close my eyes  take a bite
grab a ride  laugh out loud
there it is up on the roof
i've been there  i've survived
so just take my advice
hang in there  baby  things are crazy
but i know your future is bright
hang in there  baby
there is no maybe
eveything turns out all right
sure life is up and down
```
简化后的字幕会单独存放到after目录下，然后再对这些文件进行分析。

如果你在你的IDE中运行这些代码，你可以直接在控制台看到这些信息。

这些信息由两个部分组成，一是单词和出现的频率，二是对整个文件的分析。
例子：

```
you：1834
i：1759
the：1171
to：1113
a：1091
and：737
it：637
...
rainbow's：1
distracted：1
leslie's：1
hiya：1

--------------- 统计 ---------------
单词总使用个数：49834
出现单词个数：4099
只出现过一次的单词的个数：1938
运行时间：436
```
上面统计了20集的《查理成长日记》的词汇，居然用了436毫秒，可见我确实没有对代码进行优化。
## Getting started

**Download it! Run it!**

>Note: You can also use your favorite subtitle file, but its format should be the same as mine.

