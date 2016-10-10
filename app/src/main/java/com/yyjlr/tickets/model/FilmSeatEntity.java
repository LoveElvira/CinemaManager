package com.yyjlr.tickets.model;

import java.util.List;

/**
 * Created by Elvira on 2016/8/29.
 * 电影选座 座位表
 */
public class FilmSeatEntity {
    /**
     * "messageId": null,
     * "requestId": "7b392837-6f57-11e6-9e6c-005056c00001",
     * "error": null,
     * "statusCode": 0,
     * "cmd": "movie/querySeat",
     * "response": {
     */
    private String messageId;
    private String requestId;
    private String error;
    private String statusCode;
    private String cmd;
    public Response response;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public class Response {
        /**
         * "movieId": 1760,
         * "movieName": "",
         * "hallName": "6号厅",
         * "hallType": null,
         * "language": "英语",
         * "movieType": "3D",
         * "planId": "0100000000000027209247",
         * "playStartTime": 1461893400000,
         * "price": 30,
         * "seatType": [
         */
        private String movieId;
        private String movieName;
        private String hallName;
        private String hallType;
        private String language;
        private String movieType;
        private String planId;
        private String playStartTime;
        private String price;

        private List<SeatType> seatType;

        public class SeatType {
            /**
             * "name": "普通",
             * "type": "0",
             * "seats": 1,
             * "icon": "",
             * "isShow": "1"
             */
            private String name;
            private String type;
            private String seats;
            private String icon;
            private String isShow;

            public String getIsShow() {
                return isShow;
            }

            public void setIsShow(String isShow) {
                this.isShow = isShow;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getSeats() {
                return seats;
            }

            public void setSeats(String seats) {
                this.seats = seats;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }
        }

        //"seatList": [

        private List<SeatList> seatList;

        public class SeatList {
            /**
             * "id": "20365",
             * "row": "1",
             * "col": "9",
             * "gRow": 2,
             * "gCol": 1,
             * "type": "1",
             * "flag": "10",
             * "price": 30,
             * "relevance": ["20366"]
             */
            private String id;
            private String row;//座位 行
            private String col;//座位 列
            private int gRow;//坐标  行
            private int gCol;//坐标  列
            /**
             * 0 普通座位 0-1 可选 0-2 已选  0-3 维修
             * 1 情侣首座 0-1 可选 0-2 已选  0-3 维修
             * 2 情侣次座 0-1 可选 0-2 已选  0-3 维修
             * 3 特殊人群 0-1 可选 0-2 已选  0-3 维修
             * 4 VIP专座 0-1 可选 0-2 已选  0-3 维修
             * */
            private String type;
            private String flag;//1  不可点击  0  可点击
            private String price;
            private List relevance;//关联座位

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public List getRelevance() {
                return relevance;
            }

            public void setRelevance(List relevance) {
                this.relevance = relevance;
            }

            public SeatList() {
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getRow() {
                return row;
            }

            public void setRow(String row) {
                this.row = row;
            }

            public String getCol() {
                return col;
            }

            public void setCol(String col) {
                this.col = col;
            }

            public int getgRow() {
                return gRow;
            }

            public void setgRow(int gRow) {
                this.gRow = gRow;
            }

            public int getgCol() {
                return gCol;
            }

            public void setgCol(int gCol) {
                this.gCol = gCol;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getFlag() {
                return flag;
            }

            public void setFlag(String flag) {
                this.flag = flag;
            }
        }

        public String getMovieId() {
            return movieId;
        }

        public void setMovieId(String movieId) {
            this.movieId = movieId;
        }

        public String getMovieName() {
            return movieName;
        }

        public void setMovieName(String movieName) {
            this.movieName = movieName;
        }

        public String getHallName() {
            return hallName;
        }

        public void setHallName(String hallName) {
            this.hallName = hallName;
        }

        public String getHallType() {
            return hallType;
        }

        public void setHallType(String hallType) {
            this.hallType = hallType;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getMovieType() {
            return movieType;
        }

        public void setMovieType(String movieType) {
            this.movieType = movieType;
        }

        public String getPlanId() {
            return planId;
        }

        public void setPlanId(String planId) {
            this.planId = planId;
        }

        public String getPlayStartTime() {
            return playStartTime;
        }

        public void setPlayStartTime(String playStartTime) {
            this.playStartTime = playStartTime;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public List<SeatType> getSeatType() {
            return seatType;
        }

        public void setSeatType(List<SeatType> seatType) {
            this.seatType = seatType;
        }

        public List<SeatList> getSeatList() {
            return seatList;
        }

        public void setSeatList(List<SeatList> seatList) {
            this.seatList = seatList;
        }
    }
}
