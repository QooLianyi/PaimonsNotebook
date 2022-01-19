package com.lianyi.paimonsnotebook.bean.account;

import java.util.List;

public class AccountBean {
    /**
     * user_info :
     * follow_relation :
     * auth_relations :
     * is_in_blacklist :
     * is_has_collection :
     * is_creator : 是否是创作者
     * customer_service :
     */

    private UserInfoBean user_info;
    private Object follow_relation;
    private boolean is_in_blacklist;
    private boolean is_has_collection;
    private boolean is_creator;
    private CustomerServiceBean customer_service;
    private List<?> auth_relations;

    public UserInfoBean getUser_info() {
        return user_info;
    }

    public void setUser_info(UserInfoBean user_info) {
        this.user_info = user_info;
    }

    public Object getFollow_relation() {
        return follow_relation;
    }

    public void setFollow_relation(Object follow_relation) {
        this.follow_relation = follow_relation;
    }

    public boolean isIs_in_blacklist() {
        return is_in_blacklist;
    }

    public void setIs_in_blacklist(boolean is_in_blacklist) {
        this.is_in_blacklist = is_in_blacklist;
    }

    public boolean isIs_has_collection() {
        return is_has_collection;
    }

    public void setIs_has_collection(boolean is_has_collection) {
        this.is_has_collection = is_has_collection;
    }

    public boolean isIs_creator() {
        return is_creator;
    }

    public void setIs_creator(boolean is_creator) {
        this.is_creator = is_creator;
    }

    public CustomerServiceBean getCustomer_service() {
        return customer_service;
    }

    public void setCustomer_service(CustomerServiceBean customer_service) {
        this.customer_service = customer_service;
    }

    public List<?> getAuth_relations() {
        return auth_relations;
    }

    public void setAuth_relations(List<?> auth_relations) {
        this.auth_relations = auth_relations;
    }

    public static class UserInfoBean {
        /**
         * uid :
         * nickname :
         * introduce : 简介
         * avatar : 头像编号
         * gender :
         * certification :
         * level_exps : 等级信息
         * achieve : 账号点赞关注之类的信息
         * community_info :
         * avatar_url : 头像地址
         * certifications :
         * level_exp :
         * pendant : 头饰地址
         */

        private String uid;
        private String nickname;
        private String introduce;
        private String avatar;
        private int gender;
        private CertificationBean certification;
        private AchieveBean achieve;
        private CommunityInfoBean community_info;
        private String avatar_url;
        private Object level_exp;
        private String pendant;
        private List<LevelExpsBean> level_exps;
        private List<?> certifications;

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

        public AchieveBean getAchieve() {
            return achieve;
        }

        public void setAchieve(AchieveBean achieve) {
            this.achieve = achieve;
        }

        public CommunityInfoBean getCommunity_info() {
            return community_info;
        }

        public void setCommunity_info(CommunityInfoBean community_info) {
            this.community_info = community_info;
        }

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }

        public Object getLevel_exp() {
            return level_exp;
        }

        public void setLevel_exp(Object level_exp) {
            this.level_exp = level_exp;
        }

        public String getPendant() {
            return pendant;
        }

        public void setPendant(String pendant) {
            this.pendant = pendant;
        }

        public List<LevelExpsBean> getLevel_exps() {
            return level_exps;
        }

        public void setLevel_exps(List<LevelExpsBean> level_exps) {
            this.level_exps = level_exps;
        }

        public List<?> getCertifications() {
            return certifications;
        }

        public void setCertifications(List<?> certifications) {
            this.certifications = certifications;
        }

        public static class CertificationBean {
            /**
             * type : 0
             * label :
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

        public static class AchieveBean {
            /**
             * like_num :
             * post_num :
             * replypost_num :
             * follow_cnt :
             * followed_cnt :
             * topic_cnt :
             * new_follower_num :
             * good_post_num :
             * follow_collection_cnt :
             */

            private String like_num;
            private String post_num;
            private String replypost_num;
            private String follow_cnt;
            private String followed_cnt;
            private String topic_cnt;
            private String new_follower_num;
            private String good_post_num;
            private String follow_collection_cnt;

            public String getLike_num() {
                return like_num;
            }

            public void setLike_num(String like_num) {
                this.like_num = like_num;
            }

            public String getPost_num() {
                return post_num;
            }

            public void setPost_num(String post_num) {
                this.post_num = post_num;
            }

            public String getReplypost_num() {
                return replypost_num;
            }

            public void setReplypost_num(String replypost_num) {
                this.replypost_num = replypost_num;
            }

            public String getFollow_cnt() {
                return follow_cnt;
            }

            public void setFollow_cnt(String follow_cnt) {
                this.follow_cnt = follow_cnt;
            }

            public String getFollowed_cnt() {
                return followed_cnt;
            }

            public void setFollowed_cnt(String followed_cnt) {
                this.followed_cnt = followed_cnt;
            }

            public String getTopic_cnt() {
                return topic_cnt;
            }

            public void setTopic_cnt(String topic_cnt) {
                this.topic_cnt = topic_cnt;
            }

            public String getNew_follower_num() {
                return new_follower_num;
            }

            public void setNew_follower_num(String new_follower_num) {
                this.new_follower_num = new_follower_num;
            }

            public String getGood_post_num() {
                return good_post_num;
            }

            public void setGood_post_num(String good_post_num) {
                this.good_post_num = good_post_num;
            }

            public String getFollow_collection_cnt() {
                return follow_collection_cnt;
            }

            public void setFollow_collection_cnt(String follow_collection_cnt) {
                this.follow_collection_cnt = follow_collection_cnt;
            }
        }

        public static class CommunityInfoBean {
            /**
             * is_realname :
             * agree_status :
             * silent_end_time :
             * forbid_end_time :
             * info_upd_time :
             * privacy_invisible :
             * notify_disable :
             * has_initialized :
             * user_func_status :
             * forum_silent_info :
             */

            private boolean is_realname;
            private boolean agree_status;
            private int silent_end_time;
            private int forbid_end_time;
            private int info_upd_time;
            private PrivacyInvisibleBean privacy_invisible;
            private NotifyDisableBean notify_disable;
            private boolean has_initialized;
            private UserFuncStatusBean user_func_status;
            private List<?> forum_silent_info;

            public boolean isIs_realname() {
                return is_realname;
            }

            public void setIs_realname(boolean is_realname) {
                this.is_realname = is_realname;
            }

            public boolean isAgree_status() {
                return agree_status;
            }

            public void setAgree_status(boolean agree_status) {
                this.agree_status = agree_status;
            }

            public int getSilent_end_time() {
                return silent_end_time;
            }

            public void setSilent_end_time(int silent_end_time) {
                this.silent_end_time = silent_end_time;
            }

            public int getForbid_end_time() {
                return forbid_end_time;
            }

            public void setForbid_end_time(int forbid_end_time) {
                this.forbid_end_time = forbid_end_time;
            }

            public int getInfo_upd_time() {
                return info_upd_time;
            }

            public void setInfo_upd_time(int info_upd_time) {
                this.info_upd_time = info_upd_time;
            }

            public PrivacyInvisibleBean getPrivacy_invisible() {
                return privacy_invisible;
            }

            public void setPrivacy_invisible(PrivacyInvisibleBean privacy_invisible) {
                this.privacy_invisible = privacy_invisible;
            }

            public NotifyDisableBean getNotify_disable() {
                return notify_disable;
            }

            public void setNotify_disable(NotifyDisableBean notify_disable) {
                this.notify_disable = notify_disable;
            }

            public boolean isHas_initialized() {
                return has_initialized;
            }

            public void setHas_initialized(boolean has_initialized) {
                this.has_initialized = has_initialized;
            }

            public UserFuncStatusBean getUser_func_status() {
                return user_func_status;
            }

            public void setUser_func_status(UserFuncStatusBean user_func_status) {
                this.user_func_status = user_func_status;
            }

            public List<?> getForum_silent_info() {
                return forum_silent_info;
            }

            public void setForum_silent_info(List<?> forum_silent_info) {
                this.forum_silent_info = forum_silent_info;
            }

            public static class PrivacyInvisibleBean {
                /**
                 * post :
                 * collect :
                 * watermark :
                 * reply :
                 * post_and_instant :
                 */

                private boolean post;
                private boolean collect;
                private boolean watermark;
                private boolean reply;
                private boolean post_and_instant;

                public boolean isPost() {
                    return post;
                }

                public void setPost(boolean post) {
                    this.post = post;
                }

                public boolean isCollect() {
                    return collect;
                }

                public void setCollect(boolean collect) {
                    this.collect = collect;
                }

                public boolean isWatermark() {
                    return watermark;
                }

                public void setWatermark(boolean watermark) {
                    this.watermark = watermark;
                }

                public boolean isReply() {
                    return reply;
                }

                public void setReply(boolean reply) {
                    this.reply = reply;
                }

                public boolean isPost_and_instant() {
                    return post_and_instant;
                }

                public void setPost_and_instant(boolean post_and_instant) {
                    this.post_and_instant = post_and_instant;
                }
            }

            public static class NotifyDisableBean {
                /**
                 * reply :
                 * upvote :
                 * follow :
                 * system :
                 * chat :
                 */

                private boolean reply;
                private boolean upvote;
                private boolean follow;
                private boolean system;
                private boolean chat;

                public boolean isReply() {
                    return reply;
                }

                public void setReply(boolean reply) {
                    this.reply = reply;
                }

                public boolean isUpvote() {
                    return upvote;
                }

                public void setUpvote(boolean upvote) {
                    this.upvote = upvote;
                }

                public boolean isFollow() {
                    return follow;
                }

                public void setFollow(boolean follow) {
                    this.follow = follow;
                }

                public boolean isSystem() {
                    return system;
                }

                public void setSystem(boolean system) {
                    this.system = system;
                }

                public boolean isChat() {
                    return chat;
                }

                public void setChat(boolean chat) {
                    this.chat = chat;
                }
            }

            public static class UserFuncStatusBean {
                /**
                 * enable_history_view :
                 * enable_recommend :
                 * enable_mention :
                 */

                private boolean enable_history_view;
                private boolean enable_recommend;
                private boolean enable_mention;

                public boolean isEnable_history_view() {
                    return enable_history_view;
                }

                public void setEnable_history_view(boolean enable_history_view) {
                    this.enable_history_view = enable_history_view;
                }

                public boolean isEnable_recommend() {
                    return enable_recommend;
                }

                public void setEnable_recommend(boolean enable_recommend) {
                    this.enable_recommend = enable_recommend;
                }

                public boolean isEnable_mention() {
                    return enable_mention;
                }

                public void setEnable_mention(boolean enable_mention) {
                    this.enable_mention = enable_mention;
                }
            }
        }

        public static class LevelExpsBean {
            /**
             * level :
             * exp :
             * game_id :
             */

            private int level;
            private int exp;
            private int game_id;

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

            public int getGame_id() {
                return game_id;
            }

            public void setGame_id(int game_id) {
                this.game_id = game_id;
            }
        }
    }

    public static class CustomerServiceBean {
        /**
         * is_customer_service_staff :
         * game_id :
         */

        private boolean is_customer_service_staff;
        private int game_id;

        public boolean isIs_customer_service_staff() {
            return is_customer_service_staff;
        }

        public void setIs_customer_service_staff(boolean is_customer_service_staff) {
            this.is_customer_service_staff = is_customer_service_staff;
        }

        public int getGame_id() {
            return game_id;
        }

        public void setGame_id(int game_id) {
            this.game_id = game_id;
        }
    }
}
