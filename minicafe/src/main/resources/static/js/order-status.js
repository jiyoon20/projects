<script>
    document.addEventListener("DOMContentLoaded", function () {
        const selects = document.querySelectorAll(".status-select");

        if (selects.length === 0) {
            console.log("No select elements found");
        } else {
            console.log("Select elements found: ", selects);
        }

        selects.forEach(select => {
            select.addEventListener("change", function () {
                const orderId = this.getAttribute("data-order-id");
                const status = this.value;

                console.log('OrderId:', orderId);  // 클릭된 버튼의 주문 ID 확인
                console.log('Selected Status:', status);  // 선택된 상태 확인

                fetch("/admin/order/status", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded",
                        "X-CSRF-TOKEN": document.querySelector('meta[name="_csrf"]').getAttribute('content')
                    },
                    body: `orderId=${orderId}&orderStatus=${status}`
                })
                .then(response => response.json())
                .then(data => {
                    console.log('Response Data:', data);  // 서버 응답 확인
                    if (data.success) {
                        alert("Order status has been changed");
                        const statusCell = this.closest('tr').querySelector('.status-select');
                        statusCell.value = data.newStatus;
                        location.reload();
                    } else {
                        alert("Failed to update the order status");
                    }
                })
                .catch(error => {
                    alert("Error: " + error.message);
                });
            });
        });
    });
</script>