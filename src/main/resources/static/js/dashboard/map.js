window.onload = function() {
  var mapContainer = document.getElementById('map');
  var mapOption = {
    center: new kakao.maps.LatLng(37.5665, 126.9780), // 서울 시청
    level: 5
  };

  var map = new kakao.maps.Map(mapContainer, mapOption);
  var positions = [];

  console.log("지도 초기화 완료");

  fetch('/personal-schedule/today', {
    method: 'GET',
    credentials: 'same-origin'
  })
      .then(response => {
        console.log('Response status:', response.status);
        if (!response.ok) {
          throw new Error(`HTTP ${response.status}`);
        }
        return response.json();
      })
      .then(data => {
        console.log("받아온 데이터:", data);
        console.log("데이터 개수:", data.length);

        if (!data || data.length === 0) {
          console.log("오늘의 일정이 없습니다.");
          return;
        }

        data.forEach((item, index) => {
          console.log(`[${index}] 일정:`, {
            id: item.personalScheduleId,
            name: item.scheduleName,
            address: item.address,
            lat: item.latitude,
            lng: item.longitude
          });

          // 좌표 검증
          if (!item.latitude || !item.longitude) {
            console.warn(`[${index}] 좌표 없음 - 건너뜀`);
            return;
          }

          let scheduleDateTime = item.scheduleDateTime || '';
          let timeStr = scheduleDateTime.length >= 16 ?
              scheduleDateTime.substring(11, 16) : '';

          positions.push({
            title: `${timeStr} ${item.scheduleName}\n${item.address || ''}`,
            latlng: new kakao.maps.LatLng(item.latitude, item.longitude)
          });
        });

        console.log("최종 positions:", positions);
        console.log("positions 개수:", positions.length);

        if (positions.length === 0) {
          console.log("유효한 좌표가 없습니다.");
          return;
        }

        // 마커 생성
        var imageSrc = "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png";
        var bounds = new kakao.maps.LatLngBounds();

        for (let i = 0; i < positions.length; i++) {
          console.log(`마커 ${i} 생성:`, positions[i].latlng);

          var imageSize = new kakao.maps.Size(24, 35);
          var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);

          var marker = new kakao.maps.Marker({
            map: map,
            position: positions[i].latlng,
            title: positions[i].title,
            image: markerImage
          });

          bounds.extend(positions[i].latlng);
        }

        console.log("모든 마커 생성 완료");
        console.log("지도 범위 재설정 시작");

        // 지도 범위 재설정
        map.setBounds(bounds);

        console.log("지도 범위 재설정 완료");
      })
      .catch(error => {
        console.error('Error:', error);
        alert('일정을 불러오는데 실패했습니다: ' + error.message);
      });
}