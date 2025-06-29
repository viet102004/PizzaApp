package com.example.pizza_app.data

import com.example.pizza_app.data.model.District
import com.example.pizza_app.data.model.Province
import com.example.pizza_app.data.model.Ward



object VietnamAddressData {
    val provinces = listOf(
        Province(
            code = "01",
            name = "Hà Nội",
            districts = listOf(
                District(
                    code = "001",
                    name = "Ba Đình",
                    wards = listOf(
                        Ward("00001", "Phúc Xá"),
                        Ward("00002", "Trúc Bạch"),
                        Ward("00003", "Vĩnh Phúc"),
                        Ward("00004", "Cống Vị"),
                        Ward("00005", "Liễu Giai"),
                        Ward("00006", "Nguyễn Trung Trực"),
                        Ward("00007", "Quán Thánh"),
                        Ward("00008", "Ngọc Hà"),
                        Ward("00009", "Điện Biên"),
                        Ward("00010", "Đội Cấn"),
                        Ward("00011", "Ngọc Khánh"),
                        Ward("00012", "Kim Mã"),
                        Ward("00013", "Giảng Võ"),
                        Ward("00014", "Thành Công")
                    )
                ),
                District(
                    code = "002",
                    name = "Hoàn Kiếm",
                    wards = listOf(
                        Ward("00015", "Phúc Tân"),
                        Ward("00016", "Đồng Xuân"),
                        Ward("00017", "Hàng Mã"),
                        Ward("00018", "Hàng Buồm"),
                        Ward("00019", "Hàng Đào"),
                        Ward("00020", "Hàng Bồ"),
                        Ward("00021", "Cửa Đông"),
                        Ward("00022", "Lý Thái Tổ"),
                        Ward("00023", "Hàng Bạc"),
                        Ward("00024", "Hàng Gai"),
                        Ward("00025", "Chương Dương Độ"),
                        Ward("00026", "Hàng Trống"),
                        Ward("00027", "Cửa Nam"),
                        Ward("00028", "Hàng Bông"),
                        Ward("00029", "Tràng Tiền"),
                        Ward("00030", "Trần Hưng Đạo"),
                        Ward("00031", "Phan Chu Trinh"),
                        Ward("00032", "Hàng Bài")
                    )
                ),
                District(
                    code = "003",
                    name = "Hai Bà Trưng",
                    wards = listOf(
                        Ward("00033", "Nguyễn Du"),
                        Ward("00034", "Bạch Đằng"),
                        Ward("00035", "Phạm Đình Hổ"),
                        Ward("00036", "Lê Đại Hành"),
                        Ward("00037", "Đống Mác"),
                        Ward("00038", "Phố Huế"),
                        Ward("00039", "Hai Bà Trưng"),
                        Ward("00040", "Bùi Thị Xuân"),
                        Ward("00041", "Ngô Thì Nhậm"),
                        Ward("00042", "Lê Đại Hành"),
                        Ward("00043", "Đồng Nhân"),
                        Ward("00044", "Phạm Đình Hổ"),
                        Ward("00045", "Vĩnh Tuy"),
                        Ward("00046", "Bạch Đằng"),
                        Ward("00047", "Quỳnh Mai"),
                        Ward("00048", "Quỳnh Lôi"),
                        Ward("00049", "Minh Khai"),
                        Ward("00050", "Thanh Lương")
                    )
                )
            )
        ),
        Province(
            code = "79",
            name = "Thành phố Hồ Chí Minh",
            districts = listOf(
                District(
                    code = "760",
                    name = "Quận 1",
                    wards = listOf(
                        Ward("26734", "Tân Định"),
                        Ward("26737", "Đa Kao"),
                        Ward("26740", "Bến Nghé"),
                        Ward("26743", "Bến Thành"),
                        Ward("26746", "Nguyễn Thái Bình"),
                        Ward("26749", "Phạm Ngũ Lão"),
                        Ward("26752", "Cầu Ông Lãnh"),
                        Ward("26755", "Cô Giang"),
                        Ward("26758", "Nguyễn Cư Trinh"),
                        Ward("26761", "Cầu Kho")
                    )
                ),
                District(
                    code = "761",
                    name = "Quận 3",
                    wards = listOf(
                        Ward("26764", "Võ Thị Sáu"),
                        Ward("26767", "Đa Kao"),
                        Ward("26770", "Bến Nghé"),
                        Ward("26773", "Bến Thành"),
                        Ward("26776", "Nguyễn Thái Bình"),
                        Ward("26779", "Phạm Ngũ Lão"),
                        Ward("26782", "Cầu Ông Lãnh"),
                        Ward("26785", "Cô Giang"),
                        Ward("26788", "Nguyễn Cư Trinh"),
                        Ward("26791", "Cầu Kho"),
                        Ward("26794", "Phường 1"),
                        Ward("26797", "Phường 2"),
                        Ward("26800", "Phường 3"),
                        Ward("26803", "Phường 4"),
                        Ward("26806", "Phường 5")
                    )
                ),
                District(
                    code = "762",
                    name = "Quận 4",
                    wards = listOf(
                        Ward("26809", "Phường 1"),
                        Ward("26812", "Phường 2"),
                        Ward("26815", "Phường 3"),
                        Ward("26818", "Phường 4"),
                        Ward("26821", "Phường 6"),
                        Ward("26824", "Phường 8"),
                        Ward("26827", "Phường 9"),
                        Ward("26830", "Phường 10"),
                        Ward("26833", "Phường 13"),
                        Ward("26836", "Phường 14"),
                        Ward("26839", "Phường 15"),
                        Ward("26842", "Phường 16"),
                        Ward("26845", "Phường 18")
                    )
                )
            )
        ),
        Province(
            code = "48",
            name = "Đà Nẵng",
            districts = listOf(
                District(
                    code = "490",
                    name = "Liên Chiểu",
                    wards = listOf(
                        Ward("20194", "Hòa Hiệp Bắc"),
                        Ward("20195", "Hòa Hiệp Nam"),
                        Ward("20197", "Hòa Khánh Bắc"),
                        Ward("20198", "Hòa Khánh Nam"),
                        Ward("20200", "Hòa Minh")
                    )
                ),
                District(
                    code = "491",
                    name = "Thanh Khê",
                    wards = listOf(
                        Ward("20203", "Tam Thuận"),
                        Ward("20206", "Thanh Khê Tây"),
                        Ward("20207", "Thanh Khê Đông"),
                        Ward("20209", "Xuân Hà"),
                        Ward("20212", "Tân Chính"),
                        Ward("20215", "Chính Gián"),
                        Ward("20218", "Vĩnh Trung"),
                        Ward("20221", "Thạc Gián"),
                        Ward("20224", "An Khê"),
                        Ward("20227", "Hòa Khê")
                    )
                ),
                District(
                    code = "492",
                    name = "Hải Châu",
                    wards = listOf(
                        Ward("20230", "Thanh Bình"),
                        Ward("20233", "Thuận Phước"),
                        Ward("20236", "Thạch Thang"),
                        Ward("20239", "Hải Châu I"),
                        Ward("20242", "Hải Châu II"),
                        Ward("20245", "Phước Ninh"),
                        Ward("20248", "Hòa Thuận Tây"),
                        Ward("20251", "Hòa Thuận Đông"),
                        Ward("20254", "Nam Dương"),
                        Ward("20257", "Bình Hiên"),
                        Ward("20260", "Bình Thuận"),
                        Ward("20263", "Hòa Cường Bắc"),
                        Ward("20266", "Hòa Cường Nam")
                    )
                )
            )
        ),
        Province(
            code = "31",
            name = "Hải Phòng",
            districts = listOf(
                District(
                    code = "356",
                    name = "Hồng Bàng",
                    wards = listOf(
                        Ward("13171", "Quán Toan"),
                        Ward("13174", "Hùng Vương"),
                        Ward("13177", "Sở Dầu"),
                        Ward("13180", "Thượng Lý"),
                        Ward("13183", "Hạ Lý"),
                        Ward("13186", "Minh Khai"),
                        Ward("13189", "Trại Cau"),
                        Ward("13192", "Hoàng Văn Thụ"),
                        Ward("13195", "Phan Bội Châu")
                    )
                )
            )
        ),
        Province(
            code = "92",
            name = "Cần Thơ",
            districts = listOf(
                District(
                    code = "916",
                    name = "Ninh Kiều",
                    wards = listOf(
                        Ward("31117", "Cái Khế"),
                        Ward("31120", "An Hòa"),
                        Ward("31123", "Thới Bình"),
                        Ward("31126", "An Nghiệp"),
                        Ward("31129", "An Cư"),
                        Ward("31132", "Tân An"),
                        Ward("31135", "An Phú"),
                        Ward("31138", "Xuân Khánh"),
                        Ward("31141", "Hưng Lợi"),
                        Ward("31144", "An Khánh"),
                        Ward("31147", "An Bình")
                    )
                )
            )
        ),
        Province(
            code = "01",
            name = "Bắc Giang",
            districts = listOf(
                District(
                    code = "010",
                    name = "Thành phố Bắc Giang",
                    wards = listOf(
                        Ward("01001", "Xương Giang"),
                        Ward("01002", "Trần Nguyên Hãn"),
                        Ward("01003", "Ngô Quyền")
                    )
                ),
                District(
                    code = "011",
                    name = "Huyện Hiệp Hòa",
                    wards = listOf(
                        Ward("01101", "Thị trấn Hiệp Hòa"),
                        Ward("01102", "Đức Thắng"),
                        Ward("01103", "Hoàng An")
                    )
                ),
                District(
                    code = "012",
                    name = "Huyện Lạng Giang",
                    wards = listOf(
                        Ward("01201", "Thị trấn Vôi"),
                        Ward("01202", "Yên Mỹ"),
                        Ward("01203", "Tân Dĩnh")
                    )
                )
            )
        ),
        Province(
            code = "02",
            name = "Bắc Kạn",
            districts = listOf(
                District(
                    code = "020",
                    name = "Thành phố Bắc Kạn",
                    wards = listOf(
                        Ward("02001", "Phùng Chí Kiên"),
                        Ward("02002", "Đức Xuân"),
                        Ward("02003", "Sông Cầu")
                    )
                ),
                District(
                    code = "021",
                    name = "Huyện Bạch Thông",
                    wards = listOf(
                        Ward("02101", "Thị trấn Phủ Thông"),
                        Ward("02102", "Vi Hương"),
                        Ward("02103", "Sĩ Bình")
                    )
                ),
                District(
                    code = "022",
                    name = "Huyện Chợ Đồn",
                    wards = listOf(
                        Ward("02201", "Thị trấn Bằng Lũng"),
                        Ward("02202", "Bình Trung"),
                        Ward("02203", "Lương Bằng")
                    )
                )
            )
        ),
        Province(
            code = "03",
            name = "Bắc Ninh",
            districts = listOf(
                District(
                    code = "030",
                    name = "Thành phố Bắc Ninh",
                    wards = listOf(
                        Ward("03001", "Vệ An"),
                        Ward("03002", "Tiền An"),
                        Ward("03003", "Đại Phúc")
                    )
                ),
                District(
                    code = "031",
                    name = "Huyện Gia Bình",
                    wards = listOf(
                        Ward("03101", "Thị trấn Gia Bình"),
                        Ward("03102", "Lãng Ngâm"),
                        Ward("03103", "Nhân Thắng")
                    )
                ),
                District(
                    code = "032",
                    name = "Huyện Tiên Du",
                    wards = listOf(
                        Ward("03201", "Thị trấn Lim"),
                        Ward("03202", "Phú Lâm"),
                        Ward("03203", "Nội Duệ")
                    )
                )
            )
        ),
        Province(
            code = "04",
            name = "Cao Bằng",
            districts = listOf(
                District(
                    code = "040",
                    name = "Thành phố Cao Bằng",
                    wards = listOf(
                        Ward("04001", "Sông Bằng"),
                        Ward("04002", "Đề Thám"),
                        Ward("04003", "Hợp Giang")
                    )
                ),
                District(
                    code = "041",
                    name = "Huyện Bảo Lạc",
                    wards = listOf(
                        Ward("04101", "Thị trấn Bảo Lạc"),
                        Ward("04102", "Khánh Xuân"),
                        Ward("04103", "Cô Ba")
                    )
                ),
                District(
                    code = "042",
                    name = "Huyện Hà Quảng",
                    wards = listOf(
                        Ward("04201", "Thị trấn Xuân Hòa"),
                        Ward("04202", "Sóc Hà"),
                        Ward("04203", "Trường Hà")
                    )
                )
            )
        ),
        Province(
            code = "05",
            name = "Điện Biên",
            districts = listOf(
                District(
                    code = "050",
                    name = "Thành phố Điện Biên Phủ",
                    wards = listOf(
                        Ward("05001", "Mường Thanh"),
                        Ward("05002", "Nam Thanh"),
                        Ward("05003", "Tân Thanh")
                    )
                ),
                District(
                    code = "051",
                    name = "Huyện Điện Biên",
                    wards = listOf(
                        Ward("05101", "Thị trấn Điện Biên"),
                        Ward("05102", "Nà Tấu"),
                        Ward("05103", "Thanh Nưa")
                    )
                ),
                District(
                    code = "052",
                    name = "Huyện Mường Chà",
                    wards = listOf(
                        Ward("05201", "Thị trấn Mường Chà"),
                        Ward("05202", "Nậm Khăn"),
                        Ward("05203", "Xá Tổng")
                    )
                )
            )
        ),
        Province(
            code = "06",
            name = "Hà Giang",
            districts = listOf(
                District(
                    code = "060",
                    name = "Thành phố Hà Giang",
                    wards = listOf(
                        Ward("06001", "Nguyễn Trãi"),
                        Ward("06002", "Trần Phú"),
                        Ward("06003", "Quang Trung")
                    )
                ),
                District(
                    code = "061",
                    name = "Huyện Đồng Văn",
                    wards = listOf(
                        Ward("06101", "Thị trấn Đồng Văn"),
                        Ward("06102", "Phó Bảng"),
                        Ward("06103", "Lũng Cú")
                    )
                ),
                District(
                    code = "062",
                    name = "Huyện Mèo Vạc",
                    wards = listOf(
                        Ward("06201", "Thị trấn Mèo Vạc"),
                        Ward("06202", "Sủng Trà"),
                        Ward("06203", "Khâu Vai")
                    )
                )
            )
        ),
        Province(
            code = "07",
            name = "Hà Nam",
            districts = listOf(
                District(
                    code = "070",
                    name = "Thành phố Phủ Lý",
                    wards = listOf(
                        Ward("07001", "Lê Hồng Phong"),
                        Ward("07002", "Minh Khai"),
                        Ward("07003", "Hai Bà Trưng")
                    )
                ),
                District(
                    code = "071",
                    name = "Huyện Duy Tiên",
                    wards = listOf(
                        Ward("07101", "Thị trấn Hòa Mạc"),
                        Ward("07102", "Yên Bắc"),
                        Ward("07103", "Mộc Bắc")
                    )
                ),
                District(
                    code = "072",
                    name = "Huyện Kim Bảng",
                    wards = listOf(
                        Ward("07201", "Thị trấn Quế"),
                        Ward("07202", "Tân Sơn"),
                        Ward("07203", "Thi Sơn")
                    )
                )
            )
        ),
        Province(
            code = "08",
            name = "Hải Dương",
            districts = listOf(
                District(
                    code = "080",
                    name = "Thành phố Hải Dương",
                    wards = listOf(
                        Ward("08001", "Nguyễn Trãi"),
                        Ward("08002", "Bình Hàn"),
                        Ward("08003", "Trần Phú")
                    )
                ),
                District(
                    code = "081",
                    name = "Huyện Thanh Hà",
                    wards = listOf(
                        Ward("08101", "Thị trấn Thanh Hà"),
                        Ward("08102", "Chí Linh"),
                        Ward("08103", "Thái Học")
                    )
                ),
                District(
                    code = "082",
                    name = "Huyện Nam Sách",
                    wards = listOf(
                        Ward("08201", "Thị trấn Nam Sách"),
                        Ward("08202", "Ái Quốc"),
                        Ward("08203", "An Châu")
                    )
                )
            )
        ),
        Province(
            code = "09",
            name = "Hòa Bình",
            districts = listOf(
                District(
                    code = "090",
                    name = "Thành phố Hòa Bình",
                    wards = listOf(
                        Ward("09001", "Phương Lâm"),
                        Ward("09002", "Tân Hòa"),
                        Ward("09003", "Hữu Nghị")
                    )
                ),
                District(
                    code = "091",
                    name = "Huyện Cao Phong",
                    wards = listOf(
                        Ward("09101", "Thị trấn Cao Phong"),
                        Ward("09102", "Tân Phong"),
                        Ward("09103", "Bình Thanh")
                    )
                ),
                District(
                    code = "092",
                    name = "Huyện Đà Bắc",
                    wards = listOf(
                        Ward("09201", "Thị trấn Đà Bắc"),
                        Ward("09202", "Đồng Nghê"),
                        Ward("09203", "Tân Minh")
                    )
                )
            )
        ),
        Province(
            code = "10",
            name = "Hưng Yên",
            districts = listOf(
                District(
                    code = "100",
                    name = "Thành phố Hưng Yên",
                    wards = listOf(
                        Ward("10001", "Lê Lợi"),
                        Ward("10002", "Hiến Nam"),
                        Ward("10003", "Bần Yên Nhân")
                    )
                ),
                District(
                    code = "101",
                    name = "Huyện Văn Lâm",
                    wards = listOf(
                        Ward("10101", "Thị trấn Như Quỳnh"),
                        Ward("10102", "Lạc Đạo"),
                        Ward("10103", "Đình Dù")
                    )
                ),
                District(
                    code = "102",
                    name = "Huyện Mỹ Hào",
                    wards = listOf(
                        Ward("10201", "Thị trấn Bần Yên Nhân"),
                        Ward("10202", "Nhân Hòa"),
                        Ward("10203", "Phố Nối")
                    )
                )
            )
        ),
        Province(
            code = "11",
            name = "Lai Châu",
            districts = listOf(
                District(
                    code = "110",
                    name = "Thành phố Lai Châu",
                    wards = listOf(
                        Ward("11001", "Tân Phong"),
                        Ward("11002", "Quyết Thắng"),
                        Ward("11003", "Sông Đà")
                    )
                ),
                District(
                    code = "111",
                    name = "Huyện Tam Đường",
                    wards = listOf(
                        Ward("11101", "Thị trấn Tam Đường"),
                        Ward("11102", "Bản Giang"),
                        Ward("11103", "Nậm Loỏng")
                    )
                ),
                District(
                    code = "112",
                    name = "Huyện Mường Tè",
                    wards = listOf(
                        Ward("11201", "Thị trấn Mường Tè"),
                        Ward("11202", "Mường Tè"),
                        Ward("11203", "Nậm Khao")
                    )
                )
            )
        ),
        Province(
            code = "12",
            name = "Lạng Sơn",
            districts = listOf(
                District(
                    code = "120",
                    name = "Thành phố Lạng Sơn",
                    wards = listOf(
                        Ward("12001", "Chi Lăng"),
                        Ward("12002", "Vĩnh Trại"),
                        Ward("12003", "Tam Thanh")
                    )
                ),
                District(
                    code = "121",
                    name = "Huyện Cao Lộc",
                    wards = listOf(
                        Ward("12101", "Thị trấn Cao Lộc"),
                        Ward("12102", "Đồng Bành"),
                        Ward("12103", "Hợp Thành")
                    )
                ),
                District(
                    code = "122",
                    name = "Huyện Văn Lãng",
                    wards = listOf(
                        Ward("12201", "Thị trấn Văn Quán"),
                        Ward("12202", "Tân Thanh"),
                        Ward("12203", "Thành Công")
                    )
                )
            )
        ),
        Province(
            code = "13",
            name = "Lào Cai",
            districts = listOf(
                District(
                    code = "130",
                    name = "Thành phố Lào Cai",
                    wards = listOf(
                        Ward("13001", "Lào Cai"),
                        Ward("13002", "Duyên Hải"),
                        Ward("13003", "Kim Tân")
                    )
                ),
                District(
                    code = "131",
                    name = "Huyện Sa Pa",
                    wards = listOf(
                        Ward("13101", "Thị trấn Sa Pa"),
                        Ward("13102", "Tả Van"),
                        Ward("13103", "Mường Hoa")
                    )
                ),
                District(
                    code = "132",
                    name = "Huyện Bát Xát",
                    wards = listOf(
                        Ward("13201", "Thị trấn Bát Xát"),
                        Ward("13202", "Trịnh Tường"),
                        Ward("13203", "Mường Hum")
                    )
                )
            )
        ),
        Province(
            code = "14",
            name = "Nam Định",
            districts = listOf(
                District(
                    code = "140",
                    name = "Thành phố Nam Định",
                    wards = listOf(
                        Ward("14001", "Trần Hưng Đạo"),
                        Ward("14002", "Nguyễn Du"),
                        Ward("14003", "Lộc Vượng")
                    )
                ),
                District(
                    code = "141",
                    name = "Huyện Mỹ Lộc",
                    wards = listOf(
                        Ward("14101", "Thị trấn Mỹ Lộc"),
                        Ward("14102", "Mỹ Tân"),
                        Ward("14103", "Mỹ Hưng")
                    )
                ),
                District(
                    code = "142",
                    name = "Huyện Vụ Bản",
                    wards = listOf(
                        Ward("14201", "Thị trấn Vụ Bản"),
                        Ward("14202", "Liễu Đề"),
                        Ward("14203", "Tân Khánh")
                    )
                )
            )
        ),
        Province(
            code = "15",
            name = "Ninh Bình",
            districts = listOf(
                District(
                    code = "150",
                    name = "Thành phố Ninh Bình",
                    wards = listOf(
                        Ward("15001", "Tân Bình"),
                        Ward("15002", "Ninh Khánh"),
                        Ward("15003", "Nam Bình")
                    )
                ),
                District(
                    code = "151",
                    name = "Huyện Tam Điệp",
                    wards = listOf(
                        Ward("15101", "Thị trấn Tam Điệp"),
                        Ward("15102", "Yên Bình"),
                        Ward("15103", "Quang Sơn")
                    )
                ),
                District(
                    code = "152",
                    name = "Huyện Yên Khánh",
                    wards = listOf(
                        Ward("15201", "Thị trấn Yên Ninh"),
                        Ward("15202", "Khánh Hòa"),
                        Ward("15203", "Khánh Cường")
                    )
                )
            )
        ),
        Province(
            code = "16",
            name = "Phú Thọ",
            districts = listOf(
                District(
                    code = "160",
                    name = "Thành phố Việt Trì",
                    wards = listOf(
                        Ward("16001", "Trưng Vương"),
                        Ward("16002", "Nông Trang"),
                        Ward("16003", "Gia Cẩm")
                    )
                ),
                District(
                    code = "161",
                    name = "Huyện Đoan Hùng",
                    wards = listOf(
                        Ward("16101", "Thị trấn Đoan Hùng"),
                        Ward("16102", "Hùng Xuyên"),
                        Ward("16103", "Minh Tiến")
                    )
                ),
                District(
                    code = "162",
                    name = "Huyện Thanh Ba",
                    wards = listOf(
                        Ward("16201", "Thị trấn Thanh Ba"),
                        Ward("16202", "Tân Phương"),
                        Ward("16203", "Đại An")
                    )
                )
            )
        ),
        Province(
            code = "17",
            name = "Quảng Ninh",
            districts = listOf(
                District(
                    code = "170",
                    name = "Thành phố Hạ Long",
                    wards = listOf(
                        Ward("17001", "Bãi Cháy"),
                        Ward("17002", "Hồng Gai"),
                        Ward("17003", "Cẩm Trung")
                    )
                ),
                District(
                    code = "171",
                    name = "Huyện Cẩm Phả",
                    wards = listOf(
                        Ward("17101", "Thị trấn Cẩm Phả"),
                        Ward("17102", "Cẩm Thịnh"),
                        Ward("17103", "Cẩm Sơn")
                    )
                ),
                District(
                    code = "172",
                    name = "Huyện Vân Đồn",
                    wards = listOf(
                        Ward("17201", "Thị trấn Vân Đồn"),
                        Ward("17202", "Đồng Tiến"),
                        Ward("17203", "Bình Dân")
                    )
                )
            )
        ),
        Province(
            code = "18",
            name = "Sơn La",
            districts = listOf(
                District(
                    code = "180",
                    name = "Thành phố Sơn La",
                    wards = listOf(
                        Ward("18001", "Chiềng Lề"),
                        Ward("18002", "Chiềng Cơi"),
                        Ward("18003", "Tô Hiệu")
                    )
                ),
                District(
                    code = "181",
                    name = "Huyện Mộc Châu",
                    wards = listOf(
                        Ward("18101", "Thị trấn Mộc Châu"),
                        Ward("18102", "Chiềng Sơn"),
                        Ward("18103", "Tân Lập")
                    )
                ),
                District(
                    code = "182",
                    name = "Huyện Phù Yên",
                    wards = listOf(
                        Ward("18201", "Thị trấn Phù Yên"),
                        Ward("18202", "Tân Lang"),
                        Ward("18203", "Huy Bắc")
                    )
                )
            )
        ),
        Province(
            code = "19",
            name = "Thái Bình",
            districts = listOf(
                District(
                    code = "190",
                    name = "Thành phố Thái Bình",
                    wards = listOf(
                        Ward("19001", "Trần Lãm"),
                        Ward("19002", "Đề Thám"),
                        Ward("19003", "Kỳ Bá")
                    )
                ),
                District(
                    code = "191",
                    name = "Huyện Quỳnh Phụ",
                    wards = listOf(
                        Ward("19101", "Thị trấn Quỳnh Côi"),
                        Ward("19102", "An Khánh"),
                        Ward("19103", "Quỳnh Hoa")
                    )
                ),
                District(
                    code = "192",
                    name = "Huyện Tiền Hải",
                    wards = listOf(
                        Ward("19201", "Thị trấn Tiền Hải"),
                        Ward("19202", "Nam Hải"),
                        Ward("19203", "Đông Hải")
                    )
                )
            )
        ),
        Province(
            code = "20",
            name = "Thái Nguyên",
            districts = listOf(
                District(
                    code = "200",
                    name = "Thành phố Thái Nguyên",
                    wards = listOf(
                        Ward("20001", "Trung Thành"),
                        Ward("20002", "Tân Thịnh"),
                        Ward("20003", "Quang Trung")
                    )
                ),
                District(
                    code = "201",
                    name = "Huyện Đại Từ",
                    wards = listOf(
                        Ward("20101", "Thị trấn Hùng Sơn"),
                        Ward("20102", "Phú Lạc"),
                        Ward("20103", "Tân Thái")
                    )
                ),
                District(
                    code = "202",
                    name = "Huyện Phú Bình",
                    wards = listOf(
                        Ward("20201", "Thị trấn Phú Bình"),
                        Ward("20202", "Tân Khánh"),
                        Ward("20203", "Đồng Liên")
                    )
                )
            )
        ),
        Province(
            code = "21",
            name = "Tuyên Quang",
            districts = listOf(
                District(
                    code = "210",
                    name = "Thành phố Tuyên Quang",
                    wards = listOf(
                        Ward("21001", "Tân Quang"),
                        Ward("21002", "Phan Thiết"),
                        Ward("21003", "Đội Cấn")
                    )
                ),
                District(
                    code = "211",
                    name = "Huyện Sơn Dương",
                    wards = listOf(
                        Ward("21101", "Thị trấn Sơn Dương"),
                        Ward("21102", "Tân Trào"),
                        Ward("21103", "Đông Thọ")
                    )
                ),
                District(
                    code = "212",
                    name = "Huyện Yên Sơn",
                    wards = listOf(
                        Ward("21201", "Thị trấn Yên Sơn"),
                        Ward("21202", "Tân Yên"),
                        Ward("21203", "Hà Lang")
                    )
                )
            )
        ),
        Province(
            code = "22",
            name = "Vĩnh Phúc",
            districts = listOf(
                District(
                    code = "220",
                    name = "Thành phố Vĩnh Yên",
                    wards = listOf(
                        Ward("22001", "Đồng Tâm"),
                        Ward("22002", "Khai Quang"),
                        Ward("22003", "Nguyễn Thái Học")
                    )
                ),
                District(
                    code = "221",
                    name = "Huyện Bình Xuyên",
                    wards = listOf(
                        Ward("22101", "Thị trấn Bình Xuyên"),
                        Ward("22102", "Bình Dương"),
                        Ward("22103", "Bình Minh")
                    )
                ),
                District(
                    code = "222",
                    name = "Huyện Vĩnh Tường",
                    wards = listOf(
                        Ward("22201", "Thị trấn Vĩnh Tường"),
                        Ward("22202", "Vĩnh Thịnh"),
                        Ward("22203", "Vĩnh Ninh")
                    )
                )
            )
        ),
        Province(
            code = "23",
            name = "Yên Bái",
            districts = listOf(
                District(
                    code = "230",
                    name = "Thành phố Yên Bái",
                    wards = listOf(
                        Ward("23001", "Yên Ninh"),
                        Ward("23002", "Minh Bảo"),
                        Ward("23003", "Nguyễn Thái Học")
                    )
                ),
                District(
                    code = "231",
                    name = "Huyện Văn Yên",
                    wards = listOf(
                        Ward("23101", "Thị trấn Văn Yên"),
                        Ward("23102", "Đức Minh"),
                        Ward("23103", "Lâm Giang")
                    )
                ),
                District(
                    code = "232",
                    name = "Huyện Trấn Yên",
                    wards = listOf(
                        Ward("23201", "Thị trấn Trấn Yên"),
                        Ward("23202", "Tân Đồng"),
                        Ward("23203", "Báo Đáp")
                    )
                )
            )
        ),
        Province(
            code = "24",
            name = "Bình Định",
            districts = listOf(
                District(
                    code = "240",
                    name = "Thành phố Quy Nhơn",
                    wards = listOf(
                        Ward("24001", "Trần Phú"),
                        Ward("24002", "Lê Lợi"),
                        Ward("24003", "Nguyễn Văn Cừ")
                    )
                ),
                District(
                    code = "241",
                    name = "Huyện Hoài Nhơn",
                    wards = listOf(
                        Ward("24101", "Thị trấn Hoài Nhơn"),
                        Ward("24102", "Hoài Hảo"),
                        Ward("24103", "Hoài Châu")
                    )
                ),
                District(
                    code = "242",
                    name = "Huyện Phù Mỹ",
                    wards = listOf(
                        Ward("24201", "Thị trấn Phù Mỹ"),
                        Ward("24202", "Bình Dương"),
                        Ward("24203", "Bình Châu")
                    )
                )
            )
        ),
        Province(
            code = "25",
            name = "Bình Thuận",
            districts = listOf(
                District(
                    code = "250",
                    name = "Thành phố Phan Thiết",
                    wards = listOf(
                        Ward("25001", "Phú Hài"),
                        Ward("25002", "Mũi Né"),
                        Ward("25003", "Hàm Tiến")
                    )
                ),
                District(
                    code = "251",
                    name = "Huyện Hàm Thuận Bắc",
                    wards = listOf(
                        Ward("25101", "Thị trấn Ma Lâm"),
                        Ward("25102", "Hàm Liêm"),
                        Ward("25103", "Hàm Thắng")
                    )
                ),
                District(
                    code = "252",
                    name = "Huyện Tánh Linh",
                    wards = listOf(
                        Ward("25201", "Thị trấn Tánh Linh"),
                        Ward("25202", "Nghị Đức"),
                        Ward("25203", "Đức Phú")
                    )
                )
            )
        ),
        Province(
            code = "26",
            name = "Đắk Lắk",
            districts = listOf(
                District(
                    code = "260",
                    name = "Thành phố Buôn Ma Thuột",
                    wards = listOf(
                        Ward("26001", "Tân Lợi"),
                        Ward("26002", "Thắng Lợi"),
                        Ward("26003", "Khánh Xuân")
                    )
                ),
                District(
                    code = "261",
                    name = "Huyện Ea H'leo",
                    wards = listOf(
                        Ward("26101", "Thị trấn Ea H'leo"),
                        Ward("26102", "Ea H'leo"),
                        Ward("26103", "Ea Ral")
                    )
                ),
                District(
                    code = "262",
                    name = "Huyện Krông Pắk",
                    wards = listOf(
                        Ward("26201", "Thị trấn Phước An"),
                        Ward("26202", "Ea Kly"),
                        Ward("26203", "Krông Pắk")
                    )
                )
            )
        ),
        Province(
            code = "27",
            name = "Đắk Nông",
            districts = listOf(
                District(
                    code = "270",
                    name = "Thành phố Gia Nghĩa",
                    wards = listOf(
                        Ward("27001", "Nghĩa Trung"),
                        Ward("27002", "Nghĩa Đức"),
                        Ward("27003", "Nghĩa Phú")
                    )
                ),
                District(
                    code = "271",
                    name = "Huyện Đắk Mil",
                    wards = listOf(
                        Ward("27101", "Thị trấn Đắk Mil"),
                        Ward("27102", "Đắk R'Moan"),
                        Ward("27103", "Đắk Sắk")
                    )
                ),
                District(
                    code = "272",
                    name = "Huyện Đắk Song",
                    wards = listOf(
                        Ward("27201", "Thị trấn Đắk Song"),
                        Ward("27202", "Đắk N'Drung"),
                        Ward("27203", "Đắk Hòa")
                    )
                )
            )
        ),
        Province(
            code = "28",
            name = "Gia Lai",
            districts = listOf(
                District(
                    code = "280",
                    name = "Thành phố Pleiku",
                    wards = listOf(
                        Ward("28001", "Phù Đổng"),
                        Ward("28002", "Trà Bá"),
                        Ward("28003", "Thống Nhất")
                    )
                ),
                District(
                    code = "281",
                    name = "Huyện Chư Păh",
                    wards = listOf(
                        Ward("28101", "Thị trấn Chư Păh"),
                        Ward("28102", "Chư Đăng Ya"),
                        Ward("28103", "Ia Ka")
                    )
                ),
                District(
                    code = "282",
                    name = "Huyện Ia Grai",
                    wards = listOf(
                        Ward("28201", "Thị trấn Ia Kha"),
                        Ward("28202", "Ia Grai"),
                        Ward("28203", "Ia Piar")
                    )
                )
            )
        ),
        Province(
            code = "29",
            name = "Hà Tĩnh",
            districts = listOf(
                District(
                    code = "290",
                    name = "Thành phố Hà Tĩnh",
                    wards = listOf(
                        Ward("29001", "Nam Hà"),
                        Ward("29002", "Đức Thuận"),
                        Ward("29003", "Thạch Hạ")
                    )
                ),
                District(
                    code = "291",
                    name = "Huyện Hương Khê",
                    wards = listOf(
                        Ward("29101", "Thị trấn Hương Khê"),
                        Ward("29102", "Hương Bình"),
                        Ward("29103", "Hương Thủy")
                    )
                ),
                District(
                    code = "292",
                    name = "Huyện Nghi Xuân",
                    wards = listOf(
                        Ward("29201", "Thị trấn Nghi Xuân"),
                        Ward("29202", "Xuân Hội"),
                        Ward("29203", "Xuân Hải")
                    )
                )
            )
        ),
        Province(
            code = "30",
            name = "Khánh Hòa",
            districts = listOf(
                District(
                    code = "300",
                    name = "Thành phố Nha Trang",
                    wards = listOf(
                        Ward("30001", "Lộc Thọ"),
                        Ward("30002", "Vĩnh Hải"),
                        Ward("30003", "Phước Hòa")
                    )
                ),
                District(
                    code = "301",
                    name = "Huyện Cam Lâm",
                    wards = listOf(
                        Ward("30101", "Thị trấn Cam Đức"),
                        Ward("30102", "Cam Hải Đông"),
                        Ward("30103", "Cam Hải Tây")
                    )
                ),
                District(
                    code = "302",
                    name = "Huyện Vạn Ninh",
                    wards = listOf(
                        Ward("30201", "Thị trấn Vạn Giã"),
                        Ward("30202", "Vạn Thạnh"),
                        Ward("30203", "Vạn Phú")
                    )
                )
            )
        ),
        Province(
            code = "31",
            name = "Kon Tum",
            districts = listOf(
                District(
                    code = "310",
                    name = "Thành phố Kon Tum",
                    wards = listOf(
                        Ward("31001", "Thắng Lợi"),
                        Ward("31002", "Nguyễn Trãi"),
                        Ward("31003", "Lê Hồng Phong")
                    )
                ),
                District(
                    code = "311",
                    name = "Huyện Đăk Glei",
                    wards = listOf(
                        Ward("31101", "Thị trấn Đăk Glei"),
                        Ward("31102", "Đăk Nhoong"),
                        Ward("31103", "Đăk Pxi")
                    )
                ),
                District(
                    code = "312",
                    name = "Huyện Ngọc Hồi",
                    wards = listOf(
                        Ward("31201", "Thị trấn Ngọc Hồi"),
                        Ward("31202", "Đăk Xú"),
                        Ward("31203", "Đăk Kan")
                    )
                )
            )
        ),
        Province(
            code = "32",
            name = "Lâm Đồng",
            districts = listOf(
                District(
                    code = "320",
                    name = "Thành phố Đà Lạt",
                    wards = listOf(
                        Ward("32001", "Phường 1"),
                        Ward("32002", "Phường 2"),
                        Ward("32003", "Phường 3")
                    )
                ),
                District(
                    code = "321",
                    name = "Huyện Lạc Dương",
                    wards = listOf(
                        Ward("32101", "Thị trấn Lạc Dương"),
                        Ward("32102", "Đạ Sar"),
                        Ward("32103", "Đạ Nhim")
                    )
                ),
                District(
                    code = "322",
                    name = "Huyện Đơn Dương",
                    wards = listOf(
                        Ward("32201", "Thị trấn Đơn Dương"),
                        Ward("32202", "Ninh Gia"),
                        Ward("32203", "Tân Hội")
                    )
                )
            )
        ),
        Province(
            code = "33",
            name = "Nghệ An",
            districts = listOf(
                District(
                    code = "330",
                    name = "Thành phố Vinh",
                    wards = listOf(
                        Ward("33001", "Hưng Bình"),
                        Ward("33002", "Bến Thủy"),
                        Ward("33003", "Quang Trung")
                    )
                ),
                District(
                    code = "331",
                    name = "Huyện Nghi Lộc",
                    wards = listOf(
                        Ward("33101", "Thị trấn Nghi Lộc"),
                        Ward("33102", "Nghi Thạch"),
                        Ward("33103", "Nghi Kiều")
                    )
                ),
                District(
                    code = "332",
                    name = "Huyện Đô Lương",
                    wards = listOf(
                        Ward("33201", "Thị trấn Đô Lương"),
                        Ward("33202", "Đô Lương"),
                        Ward("33203", "Lạc Sơn")
                    )
                )
            )
        ),
        Province(
            code = "34",
            name = "Ninh Thuận",
            districts = listOf(
                District(
                    code = "340",
                    name = "Thành phố Phan Rang - Tháp Chàm",
                    wards = listOf(
                        Ward("34001", "Đô Vinh"),
                        Ward("34002", "Phước Mỹ"),
                        Ward("34003", "Bình Sơn")
                    )
                ),
                District(
                    code = "341",
                    name = "Huyện Ninh Hải",
                    wards = listOf(
                        Ward("34101", "Thị trấn Khánh Hải"),
                        Ward("34102", "Ninh Hải"),
                        Ward("34103", "Ninh Phước")
                    )
                ),
                District(
                    code = "342",
                    name = "Huyện Thuận Bắc",
                    wards = listOf(
                        Ward("34201", "Thị trấn Thiện Hải"),
                        Ward("34202", "Lợi Hải"),
                        Ward("34203", "Cà Ná")
                    )
                )
            )
        ),
        Province(
            code = "35",
            name = "Phú Yên",
            districts = listOf(
                District(
                    code = "350",
                    name = "Thành phố Tuy Hòa",
                    wards = listOf(
                        Ward("35001", "Phú Thạnh"),
                        Ward("35002", "Tân Lập"),
                        Ward("35003", "Đông Tác")
                    )
                ),
                District(
                    code = "351",
                    name = "Huyện Tuy An",
                    wards = listOf(
                        Ward("35101", "Thị trấn Chí Thạnh"),
                        Ward("35102", "An Chấn"),
                        Ward("35103", "An Ninh")
                    )
                ),
                District(
                    code = "352",
                    name = "Huyện Sông Hinh",
                    wards = listOf(
                        Ward("35201", "Thị trấn Hai Riêng"),
                        Ward("35202", "Sông Hinh"),
                        Ward("35203", "Đại Lãnh")
                    )
                )
            )
        ),
        Province(
            code = "36",
            name = "Quảng Bình",
            districts = listOf(
                District(
                    code = "360",
                    name = "Thành phố Đồng Hới",
                    wards = listOf(
                        Ward("36001", "Phường Hải Thành"),
                        Ward("36002", "Phường Đồng Phú"),
                        Ward("36003", "Phường Nam Lý")
                    )
                ),
                District(
                    code = "361",
                    name = "Huyện Quảng Trạch",
                    wards = listOf(
                        Ward("36101", "Thị trấn Quảng Trạch"),
                        Ward("36102", "Bến Vân"),
                        Ward("36103", "Cảnh Dương")
                    )
                ),
                District(
                    code = "362",
                    name = "Huyện Tuyên Hóa",
                    wards = listOf(
                        Ward("36201", "Thị trấn Đồng Lê"),
                        Ward("36202", "Tân Hóa"),
                        Ward("36203", "Mai Hóa")
                    )
                )
            )
        ),
        Province(
            code = "37",
            name = "Quảng Nam",
            districts = listOf(
                District(
                    code = "370",
                    name = "Thành phố Tam Kỳ",
                    wards = listOf(
                        Ward("37001", "Hòa Thuận"),
                        Ward("37002", "Tân Thạnh"),
                        Ward("37003", "An Mỹ")
                    )
                ),
                District(
                    code = "371",
                    name = "Huyện Núi Thành",
                    wards = listOf(
                        Ward("37101", "Thị trấn Núi Thành"),
                        Ward("37102", "Tam Nghĩa"),
                        Ward("37103", "Tam Hiệp")
                    )
                ),
                District(
                    code = "372",
                    name = "Huyện Phú Ninh",
                    wards = listOf(
                        Ward("37201", "Thị trấn Phú Thịnh"),
                        Ward("37202", "Tam Phước"),
                        Ward("37203", "Tam Lãnh")
                    )
                )
            )
        ),
        Province(
            code = "38",
            name = "Quảng Ngãi",
            districts = listOf(
                District(
                    code = "380",
                    name = "Thành phố Quảng Ngãi",
                    wards = listOf(
                        Ward("38001", "Trần Hưng Đạo"),
                        Ward("38002", "Lê Hồng Phong"),
                        Ward("38003", "Nguyễn Nghiêm")
                    )
                ),
                District(
                    code = "381",
                    name = "Huyện Tư Nghĩa",
                    wards = listOf(
                        Ward("38101", "Thị trấn La Hà"),
                        Ward("38102", "Nghĩa Kỳ"),
                        Ward("38103", "Nghĩa An")
                    )
                ),
                District(
                    code = "382",
                    name = "Huyện Sơn Tịnh",
                    wards = listOf(
                        Ward("38201", "Thị trấn Sơn Tịnh"),
                        Ward("38202", "Tịnh Ấn Tây"),
                        Ward("38203", "Tịnh Ấn Đông")
                    )
                )
            )
        ),
        Province(
            code = "39",
            name = "Quảng Trị",
            districts = listOf(
                District(
                    code = "390",
                    name = "Thành phố Đông Hà",
                    wards = listOf(
                        Ward("39001", "Đông Giang"),
                        Ward("39002", "Đông Lễ"),
                        Ward("39003", "Hải Lăng")
                    )
                ),
                District(
                    code = "391",
                    name = "Huyện Cam Lộ",
                    wards = listOf(
                        Ward("39101", "Thị trấn Cam Lộ"),
                        Ward("39102", "Cam Thành"),
                        Ward("39103", "Cam Hiếu")
                    )
                ),
                District(
                    code = "392",
                    name = "Huyện Gio Linh",
                    wards = listOf(
                        Ward("39201", "Thị trấn Gio Linh"),
                        Ward("39202", "Gio Mỹ"),
                        Ward("39203", "Gio Hải")
                    )
                )
            )
        ),
        Province(
            code = "40",
            name = "Thừa Thiên Huế",
            districts = listOf(
                District(
                    code = "400",
                    name = "Thành phố Huế",
                    wards = listOf(
                        Ward("40001", "Phú Hậu"),
                        Ward("40002", "Vĩnh Ninh"),
                        Ward("40003", "Phú Cát")
                    )
                ),
                District(
                    code = "401",
                    name = "Huyện Phú Vang",
                    wards = listOf(
                        Ward("40101", "Thị trấn Phú Đa"),
                        Ward("40102", "Vinh Thanh"),
                        Ward("40103", "Phú Hải")
                    )
                ),
                District(
                    code = "402",
                    name = "Huyện A Lưới",
                    wards = listOf(
                        Ward("40201", "Thị trấn A Lưới"),
                        Ward("40202", "Hồng Thái"),
                        Ward("40203", "A Ngo")
                    )
                )
            )
        ),
        Province(
            code = "41",
            name = "Thanh Hóa",
            districts = listOf(
                District(
                    code = "410",
                    name = "Thành phố Thanh Hóa",
                    wards = listOf(
                        Ward("41001", "Trường Thi"),
                        Ward("41002", "Đông Thọ"),
                        Ward("41003", "Nguyễn Trãi")
                    )
                ),
                District(
                    code = "411",
                    name = "Huyện Hoằng Hóa",
                    wards = listOf(
                        Ward("41101", "Thị trấn Bút Sơn"),
                        Ward("41102", "Hoằng Phú"),
                        Ward("41103", "Hoằng Trạch")
                    )
                ),
                District(
                    code = "412",
                    name = "Huyện Thọ Xuân",
                    wards = listOf(
                        Ward("41201", "Thị trấn Thọ Xuân"),
                        Ward("41202", "Thọ Lộc"),
                        Ward("41203", "Thọ Điền")
                    )
                )
            )
        ),
        Province(
            code = "42",
            name = "An Giang",
            districts = listOf(
                District(
                    code = "420",
                    name = "Thành phố Long Xuyên",
                    wards = listOf(
                        Ward("42001", "Mỹ Phước"),
                        Ward("42002", "Bình Khánh"),
                        Ward("42003", "Phú Thạnh")
                    )
                ),
                District(
                    code = "421",
                    name = "Huyện Châu Phú",
                    wards = listOf(
                        Ward("42101", "Thị trấn Châu Phú"),
                        Ward("42102", "Đông Xuyên"),
                        Ward("42103", "Châu Phú A")
                    )
                ),
                District(
                    code = "422",
                    name = "Huyện Thoại Sơn",
                    wards = listOf(
                        Ward("42201", "Thị trấn Núi Sập"),
                        Ward("42202", "Thới Sơn"),
                        Ward("42203", "Vĩnh Trạch")
                    )
                )
            )
        ),
        Province(
            code = "43",
            name = "Bà Rịa - Vũng Tàu",
            districts = listOf(
                District(
                    code = "430",
                    name = "Thành phố Vũng Tàu",
                    wards = listOf(
                        Ward("43001", "Thắng Nhất"),
                        Ward("43002", "Thắng Tam"),
                        Ward("43003", "12 Tháng 9")
                    )
                ),
                District(
                    code = "431",
                    name = "Huyện Long Điền",
                    wards = listOf(
                        Ward("43101", "Thị trấn Long Điền"),
                        Ward("43102", "An Ngãi"),
                        Ward("43103", "Long Hải")
                    )
                ),
                District(
                    code = "432",
                    name = "Huyện Đất Đỏ",
                    wards = listOf(
                        Ward("43201", "Thị trấn Đất Đỏ"),
                        Ward("43202", "Phước Hải"),
                        Ward("43203", "Long Tân")
                    )
                )
            )
        ),
        Province(
            code = "44",
            name = "Bạc Liêu",
            districts = listOf(
                District(
                    code = "440",
                    name = "Thành phố Bạc Liêu",
                    wards = listOf(
                        Ward("44001", "Phường 1"),
                        Ward("44002", "Phường 2"),
                        Ward("44003", "Phường 3")
                    )
                ),
                District(
                    code = "441",
                    name = "Huyện Bạc Liêu",
                    wards = listOf(
                        Ward("44101", "Thị trấn Bạc Liêu"),
                        Ward("44102", "Vĩnh Trạch Đông"),
                        Ward("44103", "Vĩnh Trạch")
                    )
                ),
                District(
                    code = "442",
                    name = "Huyện Hồng Dân",
                    wards = listOf(
                        Ward("44201", "Thị trấn Hồng Dân"),
                        Ward("44202", "Lộc Ninh"),
                        Ward("44203", "Ninh Quới")
                    )
                )
            )
        ),
        Province(
            code = "45",
            name = "Bến Tre",
            districts = listOf(
                District(
                    code = "450",
                    name = "Thành phố Bến Tre",
                    wards = listOf(
                        Ward("45001", "Phú Khương"),
                        Ward("45002", "Phú Tân"),
                        Ward("45003", "Nhơn Thạnh")
                    )
                ),
                District(
                    code = "451",
                    name = "Huyện Châu Thành",
                    wards = listOf(
                        Ward("45101", "Thị trấn Châu Thành"),
                        Ward("45102", "Tân Thạch"),
                        Ward("45103", "Quới Sơn")
                    )
                ),
                District(
                    code = "452",
                    name = "Huyện Mỏ Cày Bắc",
                    wards = listOf(
                        Ward("45201", "Thị trấn Mỏ Cày"),
                        Ward("45202", "An Định"),
                        Ward("45203", "Tân Thành")
                    )
                )
            )
        ),
        Province(
            code = "46",
            name = "Bình Dương",
            districts = listOf(
                District(
                    code = "460",
                    name = "Thành phố Thủ Dầu Một",
                    wards = listOf(
                        Ward("46001", "Phú Hòa"),
                        Ward("46002", "Chánh Nghĩa"),
                        Ward("46003", "Hiệp Thành")
                    )
                ),
                District(
                    code = "461",
                    name = "Huyện Bến Cát",
                    wards = listOf(
                        Ward("46101", "Thị trấn Bến Cát"),
                        Ward("46102", "Chánh Phú Hòa"),
                        Ward("46103", "Thạnh Đức")
                    )
                ),
                District(
                    code = "462",
                    name = "Huyện Dầu Tiếng",
                    wards = listOf(
                        Ward("46201", "Thị trấn Dầu Tiếng"),
                        Ward("46202", "Định Thành"),
                        Ward("46203", "Định An")
                    )
                )
            )
        ),
        Province(
            code = "47",
            name = "Bình Phước",
            districts = listOf(
                District(
                    code = "470",
                    name = "Thành phố Đồng Xoài",
                    wards = listOf(
                        Ward("47001", "Tân Bình"),
                        Ward("47002", "Tiến Hưng"),
                        Ward("47003", "Đồng Tiến")
                    )
                ),
                District(
                    code = "471",
                    name = "Huyện Bù Đốp",
                    wards = listOf(
                        Ward("47101", "Thị trấn Bù Đốp"),
                        Ward("47102", "Tân Tiến"),
                        Ward("47103", "Phước Thiện")
                    )
                ),
                District(
                    code = "472",
                    name = "Huyện Phú Riềng",
                    wards = listOf(
                        Ward("47201", "Thị trấn Phú Riềng"),
                        Ward("47202", "Bình Sơn"),
                        Ward("47203", "Phú Trung")
                    )
                )
            )
        ),
        Province(
            code = "48",
            name = "Cà Mau",
            districts = listOf(
                District(
                    code = "480",
                    name = "Thành phố Cà Mau",
                    wards = listOf(
                        Ward("48001", "Phường 1"),
                        Ward("48002", "Phường 2"),
                        Ward("48003", "Phường 3")
                    )
                ),
                District(
                    code = "481",
                    name = "Huyện Cái Nước",
                    wards = listOf(
                        Ward("48101", "Thị trấn Cái Nước"),
                        Ward("48102", "Lợi An"),
                        Ward("48103", "Hưng Mỹ")
                    )
                ),
                District(
                    code = "482",
                    name = "Huyện Đầm Dơi",
                    wards = listOf(
                        Ward("48201", "Thị trấn Đầm Dơi"),
                        Ward("48202", "Tân Duyệt"),
                        Ward("48203", "Tân Hưng")
                    )
                )
            )
        ),
        Province(
            code = "49",
            name = "Cần Thơ",
            districts = listOf(
                District(
                    code = "490",
                    name = "Thành phố Cần Thơ",
                    wards = listOf(
                        Ward("49001", "An Khánh"),
                        Ward("49002", "An Nghiệp"),
                        Ward("49003", "Hưng Phú")
                    )
                ),
                District(
                    code = "491",
                    name = "Huyện Cờ Đỏ",
                    wards = listOf(
                        Ward("49101", "Thị trấn Cờ Đỏ"),
                        Ward("49102", "Thới Hưng"),
                        Ward("49103", "Thới Đông")
                    )
                ),
                District(
                    code = "492",
                    name = "Huyện Phong Điền",
                    wards = listOf(
                        Ward("49201", "Thị trấn Phong Điền"),
                        Ward("49202", "Nhơn Ái"),
                        Ward("49203", "Tân Thới")
                    )
                )
            )
        ),
        Province(
            code = "50",
            name = "Đồng Nai",
            districts = listOf(
                District(
                    code = "500",
                    name = "Thành phố Biên Hòa",
                    wards = listOf(
                        Ward("50001", "Tân Hiệp"),
                        Ward("50002", "Long Bình"),
                        Ward("50003", "Tam Hiệp")
                    )
                ),
                District(
                    code = "501",
                    name = "Huyện Nhơn Trạch",
                    wards = listOf(
                        Ward("50101", "Thị trấn Nhơn Trạch"),
                        Ward("50102", "Phú Hội"),
                        Ward("50103", "Vĩnh Thanh")
                    )
                ),
                District(
                    code = "502",
                    name = "Huyện Long Thành",
                    wards = listOf(
                        Ward("50201", "Thị trấn Long Thành"),
                        Ward("50202", "Bình Sơn"),
                        Ward("50203", "An Phước")
                    )
                )
            )
        ),
        Province(
            code = "51",
            name = "Đồng Tháp",
            districts = listOf(
                District(
                    code = "510",
                    name = "Thành phố Cao Lãnh",
                    wards = listOf(
                        Ward("51001", "Phường 1"),
                        Ward("51002", "Phường 2"),
                        Ward("51003", "Phường 3")
                    )
                ),
                District(
                    code = "511",
                    name = "Huyện Sa Đéc",
                    wards = listOf(
                        Ward("51101", "Thị trấn Sa Đéc"),
                        Ward("51102", "Tân Khánh Đông"),
                        Ward("51103", "Tân Phú Đông")
                    )
                ),
                District(
                    code = "512",
                    name = "Huyện Lai Vung",
                    wards = listOf(
                        Ward("51201", "Thị trấn Lai Vung"),
                        Ward("51202", "Lai Vung"),
                        Ward("51203", "Long Hậu")
                    )
                )
            )
        ),
        Province(
            code = "52",
            name = "Hậu Giang",
            districts = listOf(
                District(
                    code = "520",
                    name = "Thành phố Vị Thanh",
                    wards = listOf(
                        Ward("52001", "Vị Tân"),
                        Ward("52002", "Vĩnh Tường"),
                        Ward("52003", "Hòa An")
                    )
                ),
                District(
                    code = "521",
                    name = "Huyện Châu Thành",
                    wards = listOf(
                        Ward("52101", "Thị trấn Châu Thành"),
                        Ward("52102", "Ngã Bảy"),
                        Ward("52103", "Hòa Bình")
                    )
                ),
                District(
                    code = "522",
                    name = "Huyện Long Mỹ",
                    wards = listOf(
                        Ward("52201", "Thị trấn Long Mỹ"),
                        Ward("52202", "Long Bình"),
                        Ward("52203", "Long Phú")
                    )
                )
            )
        ),Province(
            code = "53",
            name = "Kiên Giang",
            districts = listOf(
                District(
                    code = "530",
                    name = "Thành phố Rạch Giá",
                    wards = listOf(
                        Ward("53001", "Vĩnh Thanh"),
                        Ward("53002", "Vĩnh Bảo"),
                        Ward("53003", "Vĩnh Lạc")
                    )
                ),
                District(
                    code = "531",
                    name = "Huyện Hòn Đất",
                    wards = listOf(
                        Ward("53101", "Thị trấn Hòn Đất"),
                        Ward("53102", "Sóc Sơn"),
                        Ward("53103", "Bình Sơn")
                    )
                ),
                District(
                    code = "532",
                    name = "Huyện Kiên Lương",
                    wards = listOf(
                        Ward("53201", "Thị trấn Kiên Lương"),
                        Ward("53202", "Kiên Bình"),
                        Ward("53203", "Hòa Điền")
                    )
                )
            )
        ),
        Province(
            code = "54",
            name = "Long An",
            districts = listOf(
                District(
                    code = "540",
                    name = "Thành phố Tân An",
                    wards = listOf(
                        Ward("54001", "Phường 1"),
                        Ward("54002", "Phường 2"),
                        Ward("54003", "Phường 3")
                    )
                ),
                District(
                    code = "541",
                    name = "Huyện Cần Giuộc",
                    wards = listOf(
                        Ward("54101", "Thị trấn Cần Giuộc"),
                        Ward("54102", "Long Hậu"),
                        Ward("54103", "Mỹ Lộc")
                    )
                ),
                District(
                    code = "542",
                    name = "Huyện Đức Hòa",
                    wards = listOf(
                        Ward("54201", "Thị trấn Đức Hòa"),
                        Ward("54202", "Hựu Thạnh"),
                        Ward("54203", "Lộc Giang")
                    )
                )
            )
        ),
        Province(
            code = "55",
            name = "Sóc Trăng",
            districts = listOf(
                District(
                    code = "550",
                    name = "Thành phố Sóc Trăng",
                    wards = listOf(
                        Ward("55001", "Phường 1"),
                        Ward("55002", "Phường 2"),
                        Ward("55003", "Phường 3")
                    )
                ),
                District(
                    code = "551",
                    name = "Huyện Kế Sách",
                    wards = listOf(
                        Ward("55101", "Thị trấn Kế Sách"),
                        Ward("55102", "An Lạc Thôn"),
                        Ward("55103", "Ba Trinh")
                    )
                ),
                District(
                    code = "552",
                    name = "Huyện Mỹ Tú",
                    wards = listOf(
                        Ward("55201", "Thị trấn Mỹ Tú"),
                        Ward("55202", "Long Phú"),
                        Ward("55203", "Thuận Hưng")
                    )
                )
            )
        ),
        Province(
            code = "56",
            name = "Tây Ninh",
            districts = listOf(
                District(
                    code = "560",
                    name = "Thành phố Tây Ninh",
                    wards = listOf(
                        Ward("56001", "Phường 1"),
                        Ward("56002", "Phường 2"),
                        Ward("56003", "Phường 3")
                    )
                ),
                District(
                    code = "561",
                    name = "Huyện Bến Cầu",
                    wards = listOf(
                        Ward("56101", "Thị trấn Bến Cầu"),
                        Ward("56102", "Tiên Thuận"),
                        Ward("56103", "Long Khốt")
                    )
                ),
                District(
                    code = "562",
                    name = "Huyện Trảng Bàng",
                    wards = listOf(
                        Ward("56201", "Thị trấn Trảng Bàng"),
                        Ward("56202", "Hưng Thuận"),
                        Ward("56203", "An Hòa")
                    )
                )
            )
        ),
        Province(
            code = "57",
            name = "Tiền Giang",
            districts = listOf(
                District(
                    code = "570",
                    name = "Thành phố Mỹ Tho",
                    wards = listOf(
                        Ward("57001", "Phường 1"),
                        Ward("57002", "Phường 2"),
                        Ward("57003", "Phường 3")
                    )
                ),
                District(
                    code = "571",
                    name = "Huyện Cai Lậy",
                    wards = listOf(
                        Ward("57101", "Thị trấn Cai Lậy"),
                        Ward("57102", "Nhị Quý"),
                        Ward("57103", "Tân Phong")
                    )
                ),
                District(
                    code = "572",
                    name = "Huyện Châu Thành",
                    wards = listOf(
                        Ward("57201", "Thị trấn Châu Thành"),
                        Ward("57202", "Tân Hội"),
                        Ward("57203", "Tân Hương")
                    )
                )
            )
        ),
        Province(
            code = "58",
            name = "Trà Vinh",
            districts = listOf(
                District(
                    code = "580",
                    name = "Thành phố Trà Vinh",
                    wards = listOf(
                        Ward("58001", "Phường 1"),
                        Ward("58002", "Phường 2"),
                        Ward("58003", "Phường 3")
                    )
                ),
                District(
                    code = "581",
                    name = "Huyện Càng Long",
                    wards = listOf(
                        Ward("58101", "Thị trấn Càng Long"),
                        Ward("58102", "Đại Phúc"),
                        Ward("58103", "Hiệp Mỹ")
                    )
                ),
                District(
                    code = "582",
                    name = "Huyện Tiểu Cần",
                    wards = listOf(
                        Ward("58201", "Thị trấn Tiểu Cần"),
                        Ward("58202", "Tân Hòa"),
                        Ward("58203", "Hiếu Trung")
                    )
                )
            )
        ),
        Province(
            code = "59",
            name = "Vĩnh Long",
            districts = listOf(
                District(
                    code = "590",
                    name = "Thành phố Vĩnh Long",
                    wards = listOf(
                        Ward("59001", "Phường 1"),
                        Ward("59002", "Phường 2"),
                        Ward("59003", "Phường 3")
                    )
                ),
                District(
                    code = "591",
                    name = "Huyện Bình Minh",
                    wards = listOf(
                        Ward("59101", "Thị trấn Cái Vồn"),
                        Ward("59102", "Thuận An"),
                        Ward("59103", "Đông Thành")
                    )
                ),
                District(
                    code = "592",
                    name = "Huyện Tam Bình",
                    wards = listOf(
                        Ward("59201", "Thị trấn Tam Bình"),
                        Ward("59202", "Hoà Thành"),
                        Ward("59203", "Mỹ Lộc")
                    )
                )
            )
        )

        // Có thể thêm các tỉnh khác tương tự...
    )

    fun getProvinceByCode(code: String): Province? {
        return provinces.find { it.code == code }
    }

    fun getDistrictsByProvince(provinceCode: String): List<District> {
        return getProvinceByCode(provinceCode)?.districts ?: emptyList()
    }

    fun getWardsByDistrict(provinceCode: String, districtCode: String): List<Ward> {
        return getProvinceByCode(provinceCode)?.districts?.find { it.code == districtCode }?.wards ?: emptyList()
    }
}