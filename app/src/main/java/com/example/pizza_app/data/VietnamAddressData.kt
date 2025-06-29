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