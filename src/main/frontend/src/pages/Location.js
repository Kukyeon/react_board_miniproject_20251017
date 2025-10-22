import React from "react";
import "./Location.css";

function Location() {
  return (
    <div className="location-container">
      <h2>오시는 길</h2>

      <div className="location-content">
        {/* 지도 */}
        <div className="map-container">
          <iframe
            title="지도"
            src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d12651.758883260254!2d126.93487395790704!3d37.5564839526552!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x357ca150a056cc85%3A0x3e69b34abbd6445c!2z7KSR7JWZ7KCV67O07LKY66as7ZWZ7JuQL-ykkeyVmeygleuztOq4sOyIoOyduOyerOqwnOuwnOybkA!5e0!3m2!1sko!2skr!4v1761031834905!5m2!1sko!2skr"
            width="400"
            height="250"
            style={{ border: 0 }}
            allowfullscreen=""
            loading="lazy"
            referrerpolicy="no-referrer-when-downgrade"
          ></iframe>
        </div>

        {/* 텍스트 안내 */}
        <div className="location-text">
          <p>주소 : 서울특별시 마포구 신촌로 176</p>
          <p>지하철 2호선 이대역 2분</p>
          <p>영업시간 : 평일 오전 9:00~오후 6:00</p>
        </div>
      </div>
    </div>
  );
}

export default Location;
