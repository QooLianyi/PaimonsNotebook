package com.lianyi.paimonsnotebook.bean;

import java.util.List;
import java.util.Objects;

public class BlackBoardBean {

    /*
    * TODO break_type的含义:
    * TODO kind=1 break_type=0代表活动 kind=2 break_type=1代表武器 kind=2 break_type2代表角色 kind=4代表角色生日
    *
    * */

    private int retcode;
    private String message;
    private DataBean data;

    public int getRetcode() {
        return retcode;
    }

    public void setRetcode(int retcode) {
        this.retcode = retcode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {

            /**
             * title : 可莉
             * kind : 2
             * img_url : https://uploadstatic.mihoyo.com/ys-obc/2021/06/10/75276545/0e416ead4eb9fb98170fd91170f70caf_9067225062861570256.png
             * jump_type : 2
             * jump_url :
             * content_id : 55
             * style : 0
             * start_time : 0
             * end_time : 0
             * font_color :
             * padding_color :
             * drop_day : ["7","1","4"]
             * break_type : 2
             * id : 118
             * contentInfos : [{"content_id":821,"title":"「自由」的哲学","icon":"https://uploadstatic.mihoyo.com/ys-obc/2020/07/01/80410800/127e8ff10e65542d119a1b895e091b46_8324184099114143317.png","bbs_url":""},{"content_id":820,"title":"「自由」的指引","icon":"https://uploadstatic.mihoyo.com/ys-obc/2020/07/01/80410800/1145c4b5ac9012f7a30f0b7e480e5b95_3312028965203811771.png","bbs_url":""},{"content_id":819,"title":"「自由」的教导","icon":"https://uploadstatic.mihoyo.com/ys-obc/2020/07/01/80410800/2d1960fe2ce77189e18014b52982630a_2854270570499212202.png","bbs_url":""}]
             * sort : {"0":13,"1":22,"4":4}
             */

            private String title;
            private String kind;
            private String img_url;
            private String jump_type;
            private String jump_url;
            private String content_id;
            private String style;
            private String start_time;
            private String end_time;
            private String font_color;
            private String padding_color;
            private String break_type;
            private String id;
            private String sort;
            private List<String> drop_day;
            private List<ContentInfosBean> contentInfos;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getKind() {
                return kind;
            }

            public void setKind(String kind) {
                this.kind = kind;
            }

            public String getImg_url() {
                return img_url;
            }

            public void setImg_url(String img_url) {
                this.img_url = img_url;
            }

            public String getJump_type() {
                return jump_type;
            }

            public void setJump_type(String jump_type) {
                this.jump_type = jump_type;
            }

            public String getJump_url() {
                return jump_url;
            }

            public void setJump_url(String jump_url) {
                this.jump_url = jump_url;
            }

            public String getContent_id() {
                return content_id;
            }

            public void setContent_id(String content_id) {
                this.content_id = content_id;
            }

            public String getStyle() {
                return style;
            }

            public void setStyle(String style) {
                this.style = style;
            }

            public String getStart_time() {
                return start_time;
            }

            public void setStart_time(String start_time) {
                this.start_time = start_time;
            }

            public String getEnd_time() {
                return end_time;
            }

            public void setEnd_time(String end_time) {
                this.end_time = end_time;
            }

            public String getFont_color() {
                return font_color;
            }

            public void setFont_color(String font_color) {
                this.font_color = font_color;
            }

            public String getPadding_color() {
                return padding_color;
            }

            public void setPadding_color(String padding_color) {
                this.padding_color = padding_color;
            }

            public String getBreak_type() {
                return break_type;
            }

            public void setBreak_type(String break_type) {
                this.break_type = break_type;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getSort() {
                return sort;
            }

            public void setSort(String sort) {
                this.sort = sort;
            }

            public List<String> getDrop_day() {
                return drop_day;
            }

            public void setDrop_day(List<String> drop_day) {
                this.drop_day = drop_day;
            }

            public List<ContentInfosBean> getContentInfos() {
                return contentInfos;
            }

            public void setContentInfos(List<ContentInfosBean> contentInfos) {
                this.contentInfos = contentInfos;
            }

            public static class ContentInfosBean {
                /**
                 * content_id : 821
                 * title : 「自由」的哲学
                 * icon : https://uploadstatic.mihoyo.com/ys-obc/2020/07/01/80410800/127e8ff10e65542d119a1b895e091b46_8324184099114143317.png
                 * bbs_url :
                 */

                private int content_id;
                private String title;
                private String icon;
                private String bbs_url;

                public int getContent_id() {
                    return content_id;
                }

                public void setContent_id(int content_id) {
                    this.content_id = content_id;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getIcon() {
                    return icon;
                }

                public void setIcon(String icon) {
                    this.icon = icon;
                }

                public String getBbs_url() {
                    return bbs_url;
                }

                public void setBbs_url(String bbs_url) {
                    this.bbs_url = bbs_url;
                }

                @Override
                public boolean equals(Object o) {
                    if (this == o) return true;
                    if (o == null || getClass() != o.getClass()) return false;
                    ContentInfosBean that = (ContentInfosBean) o;
                    return content_id == that.content_id &&
                            Objects.equals(title, that.title) &&
                            Objects.equals(icon, that.icon) &&
                            Objects.equals(bbs_url, that.bbs_url);
                }

                @Override
                public int hashCode() {
                    return Objects.hash(content_id, title, icon, bbs_url);
                }
            }

            @Override
            public String toString() {
                return "ListBean{" +
                        "title='" + title + '\'' +
                        ", kind='" + kind + '\'' +
                        ", img_url='" + img_url + '\'' +
                        ", jump_type='" + jump_type + '\'' +
                        ", jump_url='" + jump_url + '\'' +
                        ", content_id='" + content_id + '\'' +
                        ", style='" + style + '\'' +
                        ", start_time='" + start_time + '\'' +
                        ", end_time='" + end_time + '\'' +
                        ", font_color='" + font_color + '\'' +
                        ", padding_color='" + padding_color + '\'' +
                        ", break_type='" + break_type + '\'' +
                        ", id='" + id + '\'' +
                        ", sort='" + sort + '\'' +
                        ", drop_day=" + drop_day +
                        ", contentInfos=" + contentInfos +
                        '}';
            }
        }
    }
}
