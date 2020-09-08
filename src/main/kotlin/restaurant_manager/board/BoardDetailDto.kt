package restaurant_manager.board

data class BoardDetailDto(
        var numberOfSeats: Int? = null,
        var description: String? = null,
        var employeeId: Long? = null,
        var numberOfOrders: Long? = null,
        var numberOfBookings: Long? = null

) {
    override fun toString(): String =
            "BoardDetailDto(" +
                    "numberOfSeats=$numberOfSeats, " +
                    "description=$description," +
                    " employeeId=$employeeId, " +
                    "numberOfOrders=$numberOfOrders, " +
                    "numberOfBookings=$numberOfBookings" +
                    ")"
}
