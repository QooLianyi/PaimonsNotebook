package com.lianyi.paimonsnotebook.bean.home;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeInformationBean {
    @SerializedName("recommended_topics")
    private RecommendedTopicsBean _$Recommended_topics321; // FIXME check this code
    private List<CarouselsBean> carousels;
    private List<RecommendedPostsBean> recommended_posts;
    private List<?> fixed_posts;
    private List<SelectionPostListBean> selection_post_list;

    public RecommendedTopicsBean get_$Recommended_topics321() {
        return _$Recommended_topics321;
    }

    public void set_$Recommended_topics321(RecommendedTopicsBean _$Recommended_topics321) {
        this._$Recommended_topics321 = _$Recommended_topics321;
    }

    public List<CarouselsBean> getCarousels() {
        return carousels;
    }

    public void setCarousels(List<CarouselsBean> carousels) {
        this.carousels = carousels;
    }

    public List<RecommendedPostsBean> getRecommended_posts() {
        return recommended_posts;
    }

    public void setRecommended_posts(List<RecommendedPostsBean> recommended_posts) {
        this.recommended_posts = recommended_posts;
    }

    public List<?> getFixed_posts() {
        return fixed_posts;
    }

    public void setFixed_posts(List<?> fixed_posts) {
        this.fixed_posts = fixed_posts;
    }

    public List<SelectionPostListBean> getSelection_post_list() {
        return selection_post_list;
    }

    public void setSelection_post_list(List<SelectionPostListBean> selection_post_list) {
        this.selection_post_list = selection_post_list;
    }

    public static class RecommendedTopicsBean {
        /**
         * list : [{"id":887,"name":"祝福抽签","cover":"https://upload-bbs.mihoyo.com/upload/2021/12/28/35614b7f0156046916a5d660854bb4b5_6982112189060648427.jpg","desc":"","is_focus":false,"view_num":0,"discuss_num":0},{"id":846,"name":"荒泷一斗","cover":"https://upload-bbs.mihoyo.com/upload/2021/11/13/361bf5d580d4dc12aad348f98351f7b1_2227564571396030396.png","desc":"","is_focus":false,"view_num":0,"discuss_num":0},{"id":847,"name":"五郎","cover":"https://upload-bbs.mihoyo.com/upload/2021/11/13/3f634a167c9f25230dd4fd72e18f7540_7893682481829898656.png","desc":"","is_focus":false,"view_num":0,"discuss_num":0},{"id":855,"name":"导能原盘 · 跋尾","cover":"https://upload-bbs.mihoyo.com/upload/2021/11/23/b379c0ba50a5ce286a56d2940fbbf07d_8328590925503286484.jpg","desc":"","is_focus":false,"view_num":0,"discuss_num":0},{"id":884,"name":"飞彩镌流年","cover":"https://upload-bbs.mihoyo.com/upload/2021/12/26/f3b9fa2a1909f52914d347ee2c3b2234_5419266179492041831.png","desc":"","is_focus":false,"view_num":0,"discuss_num":0},{"id":877,"name":"每日一水","cover":"https://upload-bbs.mihoyo.com/upload/2021/12/21/2d2eb14d62c2c868803e3cbe29de109b_5152187546724459447.png","desc":"","is_focus":false,"view_num":0,"discuss_num":0}]
         * position : 2
         */

        private int position;
        private List<ListBean> list;

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * id : 887
             * name : 祝福抽签
             * cover : https://upload-bbs.mihoyo.com/upload/2021/12/28/35614b7f0156046916a5d660854bb4b5_6982112189060648427.jpg
             * desc :
             * is_focus : false
             * view_num : 0
             * discuss_num : 0
             */

            private int id;
            private String name;
            private String cover;
            private String desc;
            private boolean is_focus;
            private int view_num;
            private int discuss_num;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public boolean isIs_focus() {
                return is_focus;
            }

            public void setIs_focus(boolean is_focus) {
                this.is_focus = is_focus;
            }

            public int getView_num() {
                return view_num;
            }

            public void setView_num(int view_num) {
                this.view_num = view_num;
            }

            public int getDiscuss_num() {
                return discuss_num;
            }

            public void setDiscuss_num(int discuss_num) {
                this.discuss_num = discuss_num;
            }
        }
    }

    public static class CarouselsBean {
        /**
         * cover : https://upload-bbs.mihoyo.com/upload/2021/12/30/5f7ae7e610cd41f4e335a62b8a6ef569.jpeg
         * path : /article/13834390
         */

        private String cover;
        private String path;

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }

    public static class RecommendedPostsBean {
        /**
         * post : {"game_id":2,"post_id":"13834390","f_forum_id":28,"uid":"75276539","subject":"《原神》申鹤角色PV\u2014\u2014「孤辰新梦」","content":"申鹤自幼隐居山中，师从仙家，日日静修。偶尔叶声嘈杂，鸟啼虫鸣，似有新客来访。然待侧耳细听，山寂密林深，苍风悄无言。CV：申鹤\u2014\u2014秦紫翼留云借风真君\u2014\u2014秦紫翼派蒙\u2014\u2014多多","cover":"https://upload-bbs.mihoyo.com/upload/2021/12/29/75276539/92d718f156df6d711d5a3d4bf37b6ea3_4356035390676890275.jpg","view_type":1,"created_at":1640836803,"images":["https://upload-bbs.mihoyo.com/upload/2021/12/29/75276539/6ffb8c09b5c5013a8d5f3941548a167a_8845867358773099681.jpg"],"post_status":{"is_top":false,"is_good":false,"is_official":true},"topic_ids":[884,885],"view_status":1,"max_floor":1998,"is_original":0,"republish_authorization":0,"reply_time":"2021-12-30 13:49:39","is_deleted":0,"is_interactive":false,"score":0}
         * forum : {"id":28,"name":"官方"}
         * topics : [{"id":884,"name":"飞彩镌流年","cover":"https://upload-bbs.mihoyo.com/upload/2021/12/26/f3b9fa2a1909f52914d347ee2c3b2234_5419266179492041831.png","content_type":2},{"id":885,"name":"申鹤","cover":"https://upload-bbs.mihoyo.com/upload/2021/12/27/3e4062dff7ddec6804cc9d55db6395b9_7552778112439588478.jpg","content_type":1}]
         * user : {"uid":"75276539","nickname":"西风快报员","introduce":"人类的本质是咕咕咕！","avatar":"10014","gender":0,"certification":{"type":1,"label":"唯一指定记者"},"level_exp":{"level":16,"exp":73443},"avatar_url":"https://img-static.mihoyo.com/avatar/avatar10014.png","pendant":""}
         * self_operation : null
         * stat : {"reply_num":0,"view_num":0,"like_num":0,"bookmark_num":0}
         * cover : {"url":"https://upload-bbs.mihoyo.com/upload/2021/12/29/75276539/92d718f156df6d711d5a3d4bf37b6ea3_4356035390676890275.jpg","height":0,"width":0,"format":"","size":"0","crop":null,"is_user_set_cover":false,"image_id":"0","entity_type":"IMG_ENTITY_UNKNOWN","entity_id":"0"}
         * image_list : [{"url":"https://upload-bbs.mihoyo.com/upload/2021/12/29/75276539/6ffb8c09b5c5013a8d5f3941548a167a_8845867358773099681.jpg","height":320,"width":690,"format":"jpg","size":"192562","crop":null,"is_user_set_cover":false,"image_id":"35430310","entity_type":"IMG_ENTITY_POST","entity_id":"13834390"}]
         * is_official_master : false
         * is_user_master : false
         * help_sys : {"top_up":null}
         * vote_count : 0
         * last_modify_time : 0
         * recommend_type :
         * collection : null
         * vod_list : [{"id":"1476161748493942784","duration":94507,"cover":"https://upload-bbs.mihoyo.com/upload/2021/12/29/75276539/92d718f156df6d711d5a3d4bf37b6ea3_4356035390676890275.jpg","resolutions":[{"url":"https://vod-static.mihoyo.com/1/43828081vodtranscq1500002267/75362c4e387702293727008422/v.f270753.mp4","definition":"480P","height":480,"width":852,"bitrate":405171,"size":"4786492","format":".mp4","label":"480P"},{"url":"https://vod-static.mihoyo.com/1/43828081vodtranscq1500002267/75362c4e387702293727008422/v.f270754.mp4","definition":"720P","height":720,"width":1280,"bitrate":603430,"size":"7128628","format":".mp4","label":"720P"},{"url":"https://vod-static.mihoyo.com/1/43828081vodtranscq1500002267/75362c4e387702293727008422/v.f270755.mp4","definition":"1080P","height":1080,"width":1920,"bitrate":1010391,"size":"11936255","format":".mp4","label":"1080P"}],"view_num":126264,"transcoding_status":2,"review_status":2}]
         */

        private PostBean post;
        private ForumBean forum;
        private UserBean user;
        private Object self_operation;
        private StatBean stat;
        private CoverBean cover;
        private boolean is_official_master;
        private boolean is_user_master;
        private HelpSysBean help_sys;
        private int vote_count;
        private int last_modify_time;
        private String recommend_type;
        private Object collection;
        private List<TopicsBean> topics;
        private List<ImageListBean> image_list;
        private List<VodListBean> vod_list;

        public PostBean getPost() {
            return post;
        }

        public void setPost(PostBean post) {
            this.post = post;
        }

        public ForumBean getForum() {
            return forum;
        }

        public void setForum(ForumBean forum) {
            this.forum = forum;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public Object getSelf_operation() {
            return self_operation;
        }

        public void setSelf_operation(Object self_operation) {
            this.self_operation = self_operation;
        }

        public StatBean getStat() {
            return stat;
        }

        public void setStat(StatBean stat) {
            this.stat = stat;
        }

        public CoverBean getCover() {
            return cover;
        }

        public void setCover(CoverBean cover) {
            this.cover = cover;
        }

        public boolean isIs_official_master() {
            return is_official_master;
        }

        public void setIs_official_master(boolean is_official_master) {
            this.is_official_master = is_official_master;
        }

        public boolean isIs_user_master() {
            return is_user_master;
        }

        public void setIs_user_master(boolean is_user_master) {
            this.is_user_master = is_user_master;
        }

        public HelpSysBean getHelp_sys() {
            return help_sys;
        }

        public void setHelp_sys(HelpSysBean help_sys) {
            this.help_sys = help_sys;
        }

        public int getVote_count() {
            return vote_count;
        }

        public void setVote_count(int vote_count) {
            this.vote_count = vote_count;
        }

        public int getLast_modify_time() {
            return last_modify_time;
        }

        public void setLast_modify_time(int last_modify_time) {
            this.last_modify_time = last_modify_time;
        }

        public String getRecommend_type() {
            return recommend_type;
        }

        public void setRecommend_type(String recommend_type) {
            this.recommend_type = recommend_type;
        }

        public Object getCollection() {
            return collection;
        }

        public void setCollection(Object collection) {
            this.collection = collection;
        }

        public List<TopicsBean> getTopics() {
            return topics;
        }

        public void setTopics(List<TopicsBean> topics) {
            this.topics = topics;
        }

        public List<ImageListBean> getImage_list() {
            return image_list;
        }

        public void setImage_list(List<ImageListBean> image_list) {
            this.image_list = image_list;
        }

        public List<VodListBean> getVod_list() {
            return vod_list;
        }

        public void setVod_list(List<VodListBean> vod_list) {
            this.vod_list = vod_list;
        }

        public static class PostBean {
            /**
             * game_id : 2
             * post_id : 13834390
             * f_forum_id : 28
             * uid : 75276539
             * subject : 《原神》申鹤角色PV——「孤辰新梦」
             * content : 申鹤自幼隐居山中，师从仙家，日日静修。偶尔叶声嘈杂，鸟啼虫鸣，似有新客来访。然待侧耳细听，山寂密林深，苍风悄无言。CV：申鹤——秦紫翼留云借风真君——秦紫翼派蒙——多多
             * cover : https://upload-bbs.mihoyo.com/upload/2021/12/29/75276539/92d718f156df6d711d5a3d4bf37b6ea3_4356035390676890275.jpg
             * view_type : 1
             * created_at : 1640836803
             * images : ["https://upload-bbs.mihoyo.com/upload/2021/12/29/75276539/6ffb8c09b5c5013a8d5f3941548a167a_8845867358773099681.jpg"]
             * post_status : {"is_top":false,"is_good":false,"is_official":true}
             * topic_ids : [884,885]
             * view_status : 1
             * max_floor : 1998
             * is_original : 0
             * republish_authorization : 0
             * reply_time : 2021-12-30 13:49:39
             * is_deleted : 0
             * is_interactive : false
             * score : 0
             */

            private int game_id;
            private String post_id;
            private int f_forum_id;
            private String uid;
            private String subject;
            private String content;
            private String cover;
            private int view_type;
            private int created_at;
            private PostStatusBean post_status;
            private int view_status;
            private int max_floor;
            private int is_original;
            private int republish_authorization;
            private String reply_time;
            private int is_deleted;
            private boolean is_interactive;
            private int score;
            private List<String> images;
            private List<Integer> topic_ids;

            public int getGame_id() {
                return game_id;
            }

            public void setGame_id(int game_id) {
                this.game_id = game_id;
            }

            public String getPost_id() {
                return post_id;
            }

            public void setPost_id(String post_id) {
                this.post_id = post_id;
            }

            public int getF_forum_id() {
                return f_forum_id;
            }

            public void setF_forum_id(int f_forum_id) {
                this.f_forum_id = f_forum_id;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getSubject() {
                return subject;
            }

            public void setSubject(String subject) {
                this.subject = subject;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public int getView_type() {
                return view_type;
            }

            public void setView_type(int view_type) {
                this.view_type = view_type;
            }

            public int getCreated_at() {
                return created_at;
            }

            public void setCreated_at(int created_at) {
                this.created_at = created_at;
            }

            public PostStatusBean getPost_status() {
                return post_status;
            }

            public void setPost_status(PostStatusBean post_status) {
                this.post_status = post_status;
            }

            public int getView_status() {
                return view_status;
            }

            public void setView_status(int view_status) {
                this.view_status = view_status;
            }

            public int getMax_floor() {
                return max_floor;
            }

            public void setMax_floor(int max_floor) {
                this.max_floor = max_floor;
            }

            public int getIs_original() {
                return is_original;
            }

            public void setIs_original(int is_original) {
                this.is_original = is_original;
            }

            public int getRepublish_authorization() {
                return republish_authorization;
            }

            public void setRepublish_authorization(int republish_authorization) {
                this.republish_authorization = republish_authorization;
            }

            public String getReply_time() {
                return reply_time;
            }

            public void setReply_time(String reply_time) {
                this.reply_time = reply_time;
            }

            public int getIs_deleted() {
                return is_deleted;
            }

            public void setIs_deleted(int is_deleted) {
                this.is_deleted = is_deleted;
            }

            public boolean isIs_interactive() {
                return is_interactive;
            }

            public void setIs_interactive(boolean is_interactive) {
                this.is_interactive = is_interactive;
            }

            public int getScore() {
                return score;
            }

            public void setScore(int score) {
                this.score = score;
            }

            public List<String> getImages() {
                return images;
            }

            public void setImages(List<String> images) {
                this.images = images;
            }

            public List<Integer> getTopic_ids() {
                return topic_ids;
            }

            public void setTopic_ids(List<Integer> topic_ids) {
                this.topic_ids = topic_ids;
            }

            public static class PostStatusBean {
                /**
                 * is_top : false
                 * is_good : false
                 * is_official : true
                 */

                private boolean is_top;
                private boolean is_good;
                private boolean is_official;

                public boolean isIs_top() {
                    return is_top;
                }

                public void setIs_top(boolean is_top) {
                    this.is_top = is_top;
                }

                public boolean isIs_good() {
                    return is_good;
                }

                public void setIs_good(boolean is_good) {
                    this.is_good = is_good;
                }

                public boolean isIs_official() {
                    return is_official;
                }

                public void setIs_official(boolean is_official) {
                    this.is_official = is_official;
                }
            }
        }

        public static class ForumBean {
            /**
             * id : 28
             * name : 官方
             */

            private int id;
            private String name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class UserBean {
            /**
             * uid : 75276539
             * nickname : 西风快报员
             * introduce : 人类的本质是咕咕咕！
             * avatar : 10014
             * gender : 0
             * certification : {"type":1,"label":"唯一指定记者"}
             * level_exp : {"level":16,"exp":73443}
             * avatar_url : https://img-static.mihoyo.com/avatar/avatar10014.png
             * pendant :
             */

            private String uid;
            private String nickname;
            private String introduce;
            private String avatar;
            private int gender;
            private CertificationBean certification;
            private LevelExpBean level_exp;
            private String avatar_url;
            private String pendant;

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getIntroduce() {
                return introduce;
            }

            public void setIntroduce(String introduce) {
                this.introduce = introduce;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public int getGender() {
                return gender;
            }

            public void setGender(int gender) {
                this.gender = gender;
            }

            public CertificationBean getCertification() {
                return certification;
            }

            public void setCertification(CertificationBean certification) {
                this.certification = certification;
            }

            public LevelExpBean getLevel_exp() {
                return level_exp;
            }

            public void setLevel_exp(LevelExpBean level_exp) {
                this.level_exp = level_exp;
            }

            public String getAvatar_url() {
                return avatar_url;
            }

            public void setAvatar_url(String avatar_url) {
                this.avatar_url = avatar_url;
            }

            public String getPendant() {
                return pendant;
            }

            public void setPendant(String pendant) {
                this.pendant = pendant;
            }

            public static class CertificationBean {
                /**
                 * type : 1
                 * label : 唯一指定记者
                 */

                private int type;
                private String label;

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }

                public String getLabel() {
                    return label;
                }

                public void setLabel(String label) {
                    this.label = label;
                }
            }

            public static class LevelExpBean {
                /**
                 * level : 16
                 * exp : 73443
                 */

                private int level;
                private int exp;

                public int getLevel() {
                    return level;
                }

                public void setLevel(int level) {
                    this.level = level;
                }

                public int getExp() {
                    return exp;
                }

                public void setExp(int exp) {
                    this.exp = exp;
                }
            }
        }

        public static class StatBean {
            /**
             * reply_num : 0
             * view_num : 0
             * like_num : 0
             * bookmark_num : 0
             */

            private int reply_num;
            private int view_num;
            private int like_num;
            private int bookmark_num;

            public int getReply_num() {
                return reply_num;
            }

            public void setReply_num(int reply_num) {
                this.reply_num = reply_num;
            }

            public int getView_num() {
                return view_num;
            }

            public void setView_num(int view_num) {
                this.view_num = view_num;
            }

            public int getLike_num() {
                return like_num;
            }

            public void setLike_num(int like_num) {
                this.like_num = like_num;
            }

            public int getBookmark_num() {
                return bookmark_num;
            }

            public void setBookmark_num(int bookmark_num) {
                this.bookmark_num = bookmark_num;
            }
        }

        public static class CoverBean {
            /**
             * url : https://upload-bbs.mihoyo.com/upload/2021/12/29/75276539/92d718f156df6d711d5a3d4bf37b6ea3_4356035390676890275.jpg
             * height : 0
             * width : 0
             * format :
             * size : 0
             * crop : null
             * is_user_set_cover : false
             * image_id : 0
             * entity_type : IMG_ENTITY_UNKNOWN
             * entity_id : 0
             */

            private String url;
            private int height;
            private int width;
            private String format;
            private String size;
            private Object crop;
            private boolean is_user_set_cover;
            private String image_id;
            private String entity_type;
            private String entity_id;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public String getFormat() {
                return format;
            }

            public void setFormat(String format) {
                this.format = format;
            }

            public String getSize() {
                return size;
            }

            public void setSize(String size) {
                this.size = size;
            }

            public Object getCrop() {
                return crop;
            }

            public void setCrop(Object crop) {
                this.crop = crop;
            }

            public boolean isIs_user_set_cover() {
                return is_user_set_cover;
            }

            public void setIs_user_set_cover(boolean is_user_set_cover) {
                this.is_user_set_cover = is_user_set_cover;
            }

            public String getImage_id() {
                return image_id;
            }

            public void setImage_id(String image_id) {
                this.image_id = image_id;
            }

            public String getEntity_type() {
                return entity_type;
            }

            public void setEntity_type(String entity_type) {
                this.entity_type = entity_type;
            }

            public String getEntity_id() {
                return entity_id;
            }

            public void setEntity_id(String entity_id) {
                this.entity_id = entity_id;
            }
        }

        public static class HelpSysBean {
            /**
             * top_up : null
             */

            private Object top_up;

            public Object getTop_up() {
                return top_up;
            }

            public void setTop_up(Object top_up) {
                this.top_up = top_up;
            }
        }

        public static class TopicsBean {
            /**
             * id : 884
             * name : 飞彩镌流年
             * cover : https://upload-bbs.mihoyo.com/upload/2021/12/26/f3b9fa2a1909f52914d347ee2c3b2234_5419266179492041831.png
             * content_type : 2
             */

            private int id;
            private String name;
            private String cover;
            private int content_type;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public int getContent_type() {
                return content_type;
            }

            public void setContent_type(int content_type) {
                this.content_type = content_type;
            }
        }

        public static class ImageListBean {
            /**
             * url : https://upload-bbs.mihoyo.com/upload/2021/12/29/75276539/6ffb8c09b5c5013a8d5f3941548a167a_8845867358773099681.jpg
             * height : 320
             * width : 690
             * format : jpg
             * size : 192562
             * crop : null
             * is_user_set_cover : false
             * image_id : 35430310
             * entity_type : IMG_ENTITY_POST
             * entity_id : 13834390
             */

            private String url;
            private int height;
            private int width;
            private String format;
            private String size;
            private Object crop;
            private boolean is_user_set_cover;
            private String image_id;
            private String entity_type;
            private String entity_id;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public String getFormat() {
                return format;
            }

            public void setFormat(String format) {
                this.format = format;
            }

            public String getSize() {
                return size;
            }

            public void setSize(String size) {
                this.size = size;
            }

            public Object getCrop() {
                return crop;
            }

            public void setCrop(Object crop) {
                this.crop = crop;
            }

            public boolean isIs_user_set_cover() {
                return is_user_set_cover;
            }

            public void setIs_user_set_cover(boolean is_user_set_cover) {
                this.is_user_set_cover = is_user_set_cover;
            }

            public String getImage_id() {
                return image_id;
            }

            public void setImage_id(String image_id) {
                this.image_id = image_id;
            }

            public String getEntity_type() {
                return entity_type;
            }

            public void setEntity_type(String entity_type) {
                this.entity_type = entity_type;
            }

            public String getEntity_id() {
                return entity_id;
            }

            public void setEntity_id(String entity_id) {
                this.entity_id = entity_id;
            }
        }

        public static class VodListBean {
            /**
             * id : 1476161748493942784
             * duration : 94507
             * cover : https://upload-bbs.mihoyo.com/upload/2021/12/29/75276539/92d718f156df6d711d5a3d4bf37b6ea3_4356035390676890275.jpg
             * resolutions : [{"url":"https://vod-static.mihoyo.com/1/43828081vodtranscq1500002267/75362c4e387702293727008422/v.f270753.mp4","definition":"480P","height":480,"width":852,"bitrate":405171,"size":"4786492","format":".mp4","label":"480P"},{"url":"https://vod-static.mihoyo.com/1/43828081vodtranscq1500002267/75362c4e387702293727008422/v.f270754.mp4","definition":"720P","height":720,"width":1280,"bitrate":603430,"size":"7128628","format":".mp4","label":"720P"},{"url":"https://vod-static.mihoyo.com/1/43828081vodtranscq1500002267/75362c4e387702293727008422/v.f270755.mp4","definition":"1080P","height":1080,"width":1920,"bitrate":1010391,"size":"11936255","format":".mp4","label":"1080P"}]
             * view_num : 126264
             * transcoding_status : 2
             * review_status : 2
             */

            private String id;
            private int duration;
            private String cover;
            private int view_num;
            private int transcoding_status;
            private int review_status;
            private List<ResolutionsBean> resolutions;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public int getDuration() {
                return duration;
            }

            public void setDuration(int duration) {
                this.duration = duration;
            }

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public int getView_num() {
                return view_num;
            }

            public void setView_num(int view_num) {
                this.view_num = view_num;
            }

            public int getTranscoding_status() {
                return transcoding_status;
            }

            public void setTranscoding_status(int transcoding_status) {
                this.transcoding_status = transcoding_status;
            }

            public int getReview_status() {
                return review_status;
            }

            public void setReview_status(int review_status) {
                this.review_status = review_status;
            }

            public List<ResolutionsBean> getResolutions() {
                return resolutions;
            }

            public void setResolutions(List<ResolutionsBean> resolutions) {
                this.resolutions = resolutions;
            }

            public static class ResolutionsBean {
                /**
                 * url : https://vod-static.mihoyo.com/1/43828081vodtranscq1500002267/75362c4e387702293727008422/v.f270753.mp4
                 * definition : 480P
                 * height : 480
                 * width : 852
                 * bitrate : 405171
                 * size : 4786492
                 * format : .mp4
                 * label : 480P
                 */

                private String url;
                private String definition;
                private int height;
                private int width;
                private int bitrate;
                private String size;
                private String format;
                private String label;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public String getDefinition() {
                    return definition;
                }

                public void setDefinition(String definition) {
                    this.definition = definition;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public int getBitrate() {
                    return bitrate;
                }

                public void setBitrate(int bitrate) {
                    this.bitrate = bitrate;
                }

                public String getSize() {
                    return size;
                }

                public void setSize(String size) {
                    this.size = size;
                }

                public String getFormat() {
                    return format;
                }

                public void setFormat(String format) {
                    this.format = format;
                }

                public String getLabel() {
                    return label;
                }

                public void setLabel(String label) {
                    this.label = label;
                }
            }
        }
    }

    public static class SelectionPostListBean {
        /**
         * post_id : 12354023
         * subject : 优菈 | 角色攻略、养成资源、武器选择、获取建议 攻略合集
         * forum_id : 43
         * forum_name : 攻略
         * banner : https://upload-bbs.mihoyo.com/upload/2021/11/25/80823534/fa68d0ce3475a577e398b4bb3c4b63a8_8974446651898967860.png
         */

        private String post_id;
        private String subject;
        private int forum_id;
        private String forum_name;
        private String banner;

        public String getPost_id() {
            return post_id;
        }

        public void setPost_id(String post_id) {
            this.post_id = post_id;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public int getForum_id() {
            return forum_id;
        }

        public void setForum_id(int forum_id) {
            this.forum_id = forum_id;
        }

        public String getForum_name() {
            return forum_name;
        }

        public void setForum_name(String forum_name) {
            this.forum_name = forum_name;
        }

        public String getBanner() {
            return banner;
        }

        public void setBanner(String banner) {
            this.banner = banner;
        }
    }
}
