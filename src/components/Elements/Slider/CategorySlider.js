import React from 'react';
import { Navigation, Pagination, Scrollbar, A11y, Autoplay, } from 'swiper';
import { Swiper, SwiperSlide } from 'swiper/react';

// Import Swiper styles
import 'swiper/css/bundle';
import Link from 'next/link';

const CategorySlider = () => {
    return (
        <div className="categories-area grey-2 pt-110 pb-90 position-relative">
            <div className="category-shap-02">
                <img src="assets/img/shape/categorey-shap-02.png" alt="image not found"/>
            </div>
            <div className="container">
                <div className="row">
                    <div className="col-12 text-center mb-20">
                        <div className="section-title mb-30">
                            <h2>Top <span className="down-mark-line-2">Categories</span></h2>
                        </div>
                    </div>
                    <div>
                        <div className="category-main-wrapper position-relative">
                            <div className="category-shap-01">
                                <img src="assets/img/shape/categorey-shap-01.png" alt="image not found"/>
                            </div>
                            <div>
                                <div className="category-slide-wrapper position-relative">
                                    <div className="swiper-container category-active">
                                        <div className="swiper-wrapper">
                                            <Swiper
                                                modules={[Navigation, Pagination, Scrollbar, A11y,Autoplay]}
                                                spaceBetween={30}
                                                slidesPerView={1}
                                                autoplaydisableoninteraction={"false"}
                                                loop={true}
                                                breakpoints={{
                                                    320: {
                                                        slidesPerView: 1
                                                    },
                                                    480: {
                                                        slidesPerView: 1
                                                    },
                                                    640: {
                                                        slidesPerView: 2
                                                    },
                                                    991: {
                                                        slidesPerView: 3
                                                    },
                                                    1200: {
                                                        slidesPerView: 3
                                                    },
                                                    1400: {
                                                        slidesPerView: 5
                                                    }
                                                }}
                                                autoplay= {{
                                                    delay: 3000,
                                                    disableOnInteraction: true
                                                }}
                                                navigation={{
                                                    clickable: true,
                                                    nextEl: '.category-button-next',
                                                    prevEl: '.category-button-prev',
                                            }}
                                                // pagination={{ clickable: true }}
                                                // scrollbar={{ draggable: true }}
                                                // onSwiper={(swiper) => console.log(swiper)}
                                                // onSlideChange={() => console.log("slide change")}
                                            >
                                            <SwiperSlide>
                                                <Link href="/course"><a>
                                                    <div className="categories-wrapper text-center mb-30">
                                                        <div className="categiories-thumb">
                                                            <svg xmlns="http://www.w3.org/2000/svg" width="128.484"
                                                                height="128.228" viewBox="0 0 128.484 128.228">
                                                                <g id="website-design"
                                                                    transform="translate(-23.032 -24)">
                                                                    <path id="Path_6498" data-name="Path 6498"
                                                                        d="M289.692,393.715l-4.49-17.673H272.009l4.757,22.2a11.053,11.053,0,0,0,10.808,8.737H311.8V393.715Z"
                                                                        transform="translate(-180.177 -254.763)"
                                                                        fill="#ffb3d0" />
                                                                    <path id="Path_6499" data-name="Path 6499"
                                                                        d="M272,24h53.055V96.951H272Z"
                                                                        transform="translate(-180.171)"
                                                                        fill="#339aef" />
                                                                    <path id="Path_6500" data-name="Path 6500"
                                                                        d="M272,24h53.055v8.843H272Z"
                                                                        transform="translate(-180.171)"
                                                                        fill="#74c0fc" />
                                                                    <circle id="Ellipse_26" data-name="Ellipse 26"
                                                                        cx="1.105" cy="1.105" r="1.105"
                                                                        transform="translate(96.25 27.316)"
                                                                        fill="#a4d8ff" />
                                                                    <circle id="Ellipse_27" data-name="Ellipse 27"
                                                                        cx="1.105" cy="1.105" r="1.105"
                                                                        transform="translate(100.672 27.316)"
                                                                        fill="#a4d8ff" />
                                                                    <circle id="Ellipse_28" data-name="Ellipse 28"
                                                                        cx="1.105" cy="1.105" r="1.105"
                                                                        transform="translate(105.093 27.316)"
                                                                        fill="#a4d8ff" />
                                                                    <path id="Path_6501" data-name="Path 6501"
                                                                        d="M48,200H71.212v13.264H48Z"
                                                                        transform="translate(-18.069 -127.366)"
                                                                        fill="#a4d8ff" />
                                                                    <g id="Group_2240" data-name="Group 2240"
                                                                        transform="translate(32.142 39.474)">
                                                                        <path id="Path_6502" data-name="Path 6502"
                                                                            d="M61.527,216.842a1.1,1.1,0,0,1-.663-.221l-4.421-3.316a1.105,1.105,0,0,1,0-1.769l4.421-3.316a1.105,1.105,0,0,1,1.326,1.768l-3.242,2.432,3.242,2.432a1.105,1.105,0,0,1-.663,1.99Z"
                                                                            transform="translate(-56 -172.63)"
                                                                            fill="#74c0fc" />
                                                                        <path id="Path_6503" data-name="Path 6503"
                                                                            d="M105.1,216.842a1.105,1.105,0,0,1-.79-1.879l2.489-2.542-2.489-2.542a1.106,1.106,0,0,1,1.579-1.547l3.247,3.316a1.105,1.105,0,0,1,0,1.547l-3.247,3.316A1.1,1.1,0,0,1,105.1,216.842Z"
                                                                            transform="translate(-90.735 -172.629)"
                                                                            fill="#74c0fc" />
                                                                        <path id="Path_6504" data-name="Path 6504"
                                                                            d="M85.1,216.841a1.105,1.105,0,0,1-1.05-1.455l2.211-6.632a1.105,1.105,0,1,1,2.1.7l-2.211,6.632A1.105,1.105,0,0,1,85.1,216.841Z"
                                                                            transform="translate(-76.259 -172.628)"
                                                                            fill="#74c0fc" />
                                                                        <circle id="Ellipse_29" data-name="Ellipse 29"
                                                                            cx="3.316" cy="3.316" r="3.316"
                                                                            transform="translate(70.74 0)"
                                                                            fill="#74c0fc" />
                                                                        <path id="Path_6505" data-name="Path 6505"
                                                                            d="M381.422,82.211H357.105a1.105,1.105,0,0,1,0-2.211h24.317a1.105,1.105,0,0,1,0,2.211Z"
                                                                            transform="translate(-273.101 -80)"
                                                                            fill="#74c0fc" />
                                                                        <path id="Path_6506" data-name="Path 6506"
                                                                            d="M368.158,98.211H357.105a1.105,1.105,0,1,1,0-2.211h11.053a1.105,1.105,0,1,1,0,2.211Z"
                                                                            transform="translate(-273.101 -91.579)"
                                                                            fill="#74c0fc" />
                                                                        <circle id="Ellipse_30" data-name="Ellipse 30"
                                                                            cx="3.316" cy="3.316" r="3.316"
                                                                            transform="translate(70.74 11.053)"
                                                                            fill="#74c0fc" />
                                                                        <path id="Path_6507" data-name="Path 6507"
                                                                            d="M381.422,122.211H357.105a1.105,1.105,0,1,1,0-2.211h24.317a1.105,1.105,0,0,1,0,2.211Z"
                                                                            transform="translate(-273.101 -108.947)"
                                                                            fill="#74c0fc" />
                                                                        <path id="Path_6508" data-name="Path 6508"
                                                                            d="M368.158,138.211H357.105a1.105,1.105,0,1,1,0-2.211h11.053a1.105,1.105,0,0,1,0,2.211Z"
                                                                            transform="translate(-273.101 -120.526)"
                                                                            fill="#74c0fc" />
                                                                    </g>
                                                                    <path id="Path_6509" data-name="Path 6509"
                                                                        d="M166.821,320.074a10.644,10.644,0,0,0-10.326-8.062L112.769,312l-1.019,17.685v30.96h46.423V329.7h11.053Z"
                                                                        transform="translate(-64.203 -208.417)"
                                                                        fill="#855ef7" />
                                                                    <path id="Path_6510" data-name="Path 6510"
                                                                        d="M187.053,274.369V260H176v14.369l5.527,4.421Z"
                                                                        transform="translate(-110.698 -170.786)"
                                                                        fill="#ffb3d0" />
                                                                    <path id="Path_6511" data-name="Path 6511"
                                                                        d="M197.307,293.979a7.428,7.428,0,0,1-7.046-5.079l-.092-.275h7.138Z"
                                                                        transform="translate(-120.952 -191.501)"
                                                                        fill="#e67199" />
                                                                    <path id="Path_6512" data-name="Path 6512"
                                                                        d="M191.474,238.306v9.222a6.79,6.79,0,0,1-6.79,6.79A8.684,8.684,0,0,1,176,245.634v-7.329a1.431,1.431,0,0,1,1.431-1.431h12.613A1.431,1.431,0,0,1,191.474,238.306Z"
                                                                        transform="translate(-110.698 -154.051)"
                                                                        fill="#ffb3d0" />
                                                                    <path id="Path_6513" data-name="Path 6513"
                                                                        d="M190.383,210.211v4.311a5.637,5.637,0,0,1-7.564,5.3l-8.187-2.976v1.105A6.632,6.632,0,0,1,168,224.58v-7.546a9.034,9.034,0,0,1,12.88-8.174l2.871,1.351Z"
                                                                        transform="translate(-104.909 -133.156)"
                                                                        fill="#0e3e86" />
                                                                    <circle id="Ellipse_31" data-name="Ellipse 31"
                                                                        cx="2.211" cy="2.211" r="2.211"
                                                                        transform="translate(63.091 89.214)"
                                                                        fill="#ffb3d0" />
                                                                    <path id="Path_6514" data-name="Path 6514"
                                                                        d="M248,472h31.916v4.418H248Z"
                                                                        transform="translate(-162.803 -324.205)"
                                                                        fill="#e67701" />
                                                                    <path id="Path_6515" data-name="Path 6515"
                                                                        d="M187.737,333.105l-1.105,4.421,2.211,37.578-8.843,0,2.211-37.581-1.105-4.421L184.421,332Z"
                                                                        transform="translate(-113.593 -222.891)"
                                                                        fill="#f2444e" />
                                                                    <path id="Path_6516" data-name="Path 6516"
                                                                        d="M35.17,345.546l-3.156,8.415L23.7,373.355a8.29,8.29,0,0,0,7.619,11.556h10.54a75.278,75.278,0,0,0,11.448-.875l23.048-3.546H91.832l-5.157-6.875a4.919,4.919,0,0,0-3.935-1.967H40.856l6.632-17.685Z"
                                                                        transform="translate(0 -232.694)"
                                                                        fill="#ffb3d0" />
                                                                    <path id="Path_6517" data-name="Path 6517"
                                                                        d="M71.01,329.685H55.536l3.156-8.415A14.286,14.286,0,0,1,72.068,312a3.863,3.863,0,0,1,3.617,5.219Z"
                                                                        transform="translate(-23.522 -208.417)"
                                                                        fill="#855ef7" />
                                                                    <path id="Path_6518" data-name="Path 6518"
                                                                        d="M166.79,324.843a1.1,1.1,0,0,1-.569-.158l-4.958-2.975-4.958,2.975a1.105,1.105,0,0,1-1.35-.166l-6.632-6.632a1.105,1.105,0,1,1,1.563-1.563l6.022,6.022,4.787-2.872a1.105,1.105,0,0,1,1.137,0l4.787,2.872,6.022-6.022a1.105,1.105,0,1,1,1.563,1.563l-6.632,6.632A1.1,1.1,0,0,1,166.79,324.843Z"
                                                                        transform="translate(-90.436 -211.313)"
                                                                        fill="#603ec5" />
                                                                    <path id="Path_6519" data-name="Path 6519"
                                                                        d="M144,76h42v25.422H144Z"
                                                                        transform="translate(-87.541 -37.631)"
                                                                        fill="#a4d8ff" />
                                                                    <path id="Path_6520" data-name="Path 6520"
                                                                        d="M144,121.685v-8.843L157.264,104l26.528,17.685Z"
                                                                        transform="translate(-87.541 -57.894)"
                                                                        fill="#339aef" />
                                                                    <circle id="Ellipse_32" data-name="Ellipse 32"
                                                                        cx="3.178" cy="3.178" r="3.178"
                                                                        transform="translate(82.019 42.929)"
                                                                        fill="#f5a000" />
                                                                    <path id="Path_6521" data-name="Path 6521"
                                                                        d="M224,133.264,237.264,120l6.632,6.632v6.632Z"
                                                                        transform="translate(-145.435 -69.472)"
                                                                        fill="#74c0fc" />
                                                                    <path id="Path_6522" data-name="Path 6522"
                                                                        d="M344,168h39.791v26.528H344Z"
                                                                        transform="translate(-232.275 -104.209)"
                                                                        fill="#74c0fc" />
                                                                    <path id="Path_6523" data-name="Path 6523"
                                                                        d="M389.948,199.9a9.948,9.948,0,1,1,9.948-9.948A9.948,9.948,0,0,1,389.948,199.9Zm0-17.685a7.737,7.737,0,1,0,7.737,7.737A7.737,7.737,0,0,0,389.948,182.211Z"
                                                                        transform="translate(-258.327 -112.893)"
                                                                        fill="#a4d8ff" />
                                                                    <path id="Path_6524" data-name="Path 6524"
                                                                        d="M408,200v8.843l6.632-4.421Z"
                                                                        transform="translate(-278.59 -127.366)"
                                                                        fill="#f2444e" />
                                                                    <path id="Path_6525" data-name="Path 6525"
                                                                        d="M377.467,430.106H343.162L348.879,408h34.305Z"
                                                                        transform="translate(-231.669 -277.89)"
                                                                        fill="#fcc419" />
                                                                    <circle id="Ellipse_33" data-name="Ellipse 33"
                                                                        cx="3.158" cy="3.158" r="3.158"
                                                                        transform="translate(128.346 138.005)"
                                                                        fill="#f5a000" />
                                                                </g>
                                                            </svg>
                                                        </div>
                                                        <div className="categories-content">
                                                            <h4>Business</h4>
                                                            <p>13 Course available</p>
                                                        </div>
                                                    </div>
                                                </a></Link>
                                            </SwiperSlide>
                                            <SwiperSlide>
                                                <Link href="/course"><a>
                                                    <div className="categories-wrapper text-center mb-30">
                                                        <div className="categiories-thumb">
                                                            <svg xmlns="http://www.w3.org/2000/svg" width="120.229"
                                                                height="128.228" viewBox="0 0 120.229 128.228">
                                                                <g id="analytics" transform="translate(-15.971)">
                                                                    <rect id="Rectangle_108" data-name="Rectangle 108"
                                                                        width="26.744" height="13.371"
                                                                        transform="translate(65.276 113.511)"
                                                                        fill="#c1ced4" />
                                                                    <rect id="Rectangle_109" data-name="Rectangle 109"
                                                                        width="39.582" height="2.689"
                                                                        transform="translate(58.858 125.539)"
                                                                        fill="#2e3133" />
                                                                    <path id="Path_6526" data-name="Path 6526"
                                                                        d="M174.684,165.5a3.2,3.2,0,0,0-2.27-.941H87.9a3.207,3.207,0,0,0-3.2,3.209v58.387h90.928V167.765A3.2,3.2,0,0,0,174.684,165.5Zm-4.943,54.775H90.579V170.439h79.162v49.833Z"
                                                                        transform="translate(-51.514 -123.345)"
                                                                        fill="#c1ced4" />
                                                                    <g id="Group_2241" data-name="Group 2241"
                                                                        transform="translate(21.487 50.962)">
                                                                        <path id="Path_6527" data-name="Path 6527"
                                                                            d="M84.7,410.506V418a3.211,3.211,0,0,0,3.2,3.21h84.515a3.215,3.215,0,0,0,3.21-3.21v-7.492ZM172.934,418a.5.5,0,0,1-.148.36.524.524,0,0,1-.372.16H87.9a.517.517,0,0,1-.366-.158.5.5,0,0,1-.147-.362v-4.8h85.548V418Z"
                                                                            transform="translate(-73.001 -358.661)"
                                                                            fill="#2e3133" />
                                                                        <path id="Path_6528" data-name="Path 6528"
                                                                            d="M150.414,205.064,138.5,216.977l-7.079-7.084-14.666,12.292-3.289-3.289-6.569,6.57-6.95-6.945L85.725,232.742l-5.175-5.171s-10.381,10.38-10.587,10.589l-10.18-10.175L38,249.77l1.9,1.9,19.884-19.884,10.18,10.174L80.55,231.374l5.175,5.17,14.219-14.219,6.95,6.945,6.569-6.57,3.13,3.13,14.664-12.293,7.238,7.243,13.82-13.815Z"
                                                                            transform="translate(-37.997 -204.67)"
                                                                            fill="#2e3133" />
                                                                        <path id="Path_6529" data-name="Path 6529"
                                                                            d="M470.053,212.172h-2.691v-5.993H461.37v-2.69h8.683Z"
                                                                            transform="translate(-355.341 -203.489)"
                                                                            fill="#2e3133" />
                                                                    </g>
                                                                    <path id="Path_6530" data-name="Path 6530"
                                                                        d="M155.448,292.769l-5.087-5.089-10.425,8.737-2.282-2.282-4.67,4.668-4.935-4.934-10.107,10.1-3.678-3.675-7.527,7.527L99.5,300.594,84.7,315.4h79.9V283.625Z"
                                                                        transform="translate(-51.514 -212.594)"
                                                                        fill="#f95632" />
                                                                    <path id="Path_6531" data-name="Path 6531"
                                                                        d="M15.971,114.55v34.072h6.537v8.991l8.992-8.991H74.047V114.55Z"
                                                                        transform="translate(0 -85.862)"
                                                                        fill="#63d1db" />
                                                                    <path id="Path_6532" data-name="Path 6532"
                                                                        d="M90.579,186.1V170.438h34.982v-5.882H87.9a3.207,3.207,0,0,0-3.2,3.209V186.1h5.882Z"
                                                                        transform="translate(-51.514 -123.345)"
                                                                        fill="#4ba9b6" />
                                                                    <g id="Group_2242" data-name="Group 2242"
                                                                        transform="translate(20.409 31.216)">
                                                                        <rect id="Rectangle_110"
                                                                            data-name="Rectangle 110" width="3.449"
                                                                            height="4.653"
                                                                            transform="translate(0 19.965)"
                                                                            fill="#fff" />
                                                                        <rect id="Rectangle_111"
                                                                            data-name="Rectangle 111" width="3.449"
                                                                            height="8.965"
                                                                            transform="translate(4.551 15.65)"
                                                                            fill="#fff" />
                                                                        <rect id="Rectangle_112"
                                                                            data-name="Rectangle 112" width="3.452"
                                                                            height="6.975"
                                                                            transform="translate(9.099 17.644)"
                                                                            fill="#fff" />
                                                                        <rect id="Rectangle_113"
                                                                            data-name="Rectangle 113" width="3.448"
                                                                            height="16.923"
                                                                            transform="translate(13.652 7.696)"
                                                                            fill="#fff" />
                                                                        <rect id="Rectangle_114"
                                                                            data-name="Rectangle 114" width="3.449"
                                                                            height="12.432"
                                                                            transform="translate(18.201 12.188)"
                                                                            fill="#fff" />
                                                                        <rect id="Rectangle_115"
                                                                            data-name="Rectangle 115" width="3.447"
                                                                            height="20.289"
                                                                            transform="translate(22.753 4.328)"
                                                                            fill="#fff" />
                                                                        <rect id="Rectangle_116"
                                                                            data-name="Rectangle 116" width="3.45"
                                                                            height="10.549"
                                                                            transform="translate(27.301 14.073)"
                                                                            fill="#fff" />
                                                                        <rect id="Rectangle_117"
                                                                            data-name="Rectangle 117" width="3.448"
                                                                            height="8.464"
                                                                            transform="translate(31.852 16.155)"
                                                                            fill="#fff" />
                                                                        <rect id="Rectangle_118"
                                                                            data-name="Rectangle 118" width="3.45"
                                                                            height="16.923"
                                                                            transform="translate(36.4 7.696)"
                                                                            fill="#fff" />
                                                                        <rect id="Rectangle_119"
                                                                            data-name="Rectangle 119" width="3.451"
                                                                            height="24.619" transform="translate(40.95)"
                                                                            fill="#fff" />
                                                                    </g>
                                                                    <path id="Path_6533" data-name="Path 6533"
                                                                        d="M280.574,37.5A12.208,12.208,0,1,0,292.785,49.7,12.2,12.2,0,0,0,280.574,37.5Z"
                                                                        transform="translate(-189.187 -28.106)"
                                                                        fill="#c1ced4" />
                                                                    <path id="Path_6534" data-name="Path 6534"
                                                                        d="M266.712,24.253,248.534,8.436l.882-1.014A21.6,21.6,0,0,1,268.547.187l1.332.178ZM252.363,8.2,264.7,18.94l2.15-16.215A18.856,18.856,0,0,0,252.363,8.2Z"
                                                                        transform="translate(-174.32 0)"
                                                                        fill="#2e3133" />
                                                                    <path id="Path_6535" data-name="Path 6535"
                                                                        d="M314.472,113.133c.221.01.437.034.657.033a14.515,14.515,0,0,0,9.1-3.191l-5.756-7.289a5.279,5.279,0,0,1-3.322,1.184c-.1,0-.189-.007-.29-.012Z"
                                                                        transform="translate(-223.745 -76.969)"
                                                                        fill="#f95632" />
                                                                    <g id="Group_2243" data-name="Group 2243"
                                                                        transform="translate(92.733 14.844)">
                                                                        <path id="Path_6536" data-name="Path 6536"
                                                                            d="M331.945,63.858a5.3,5.3,0,0,1-1.507,6.284l7.077,8.734A16.545,16.545,0,0,0,342.2,59.271Z"
                                                                            transform="translate(-328.446 -59.271)"
                                                                            fill="#63d1db" />
                                                                        <circle id="Ellipse_34" data-name="Ellipse 34"
                                                                            cx="3.669" cy="3.669" r="3.669"
                                                                            transform="translate(0 36.406)"
                                                                            fill="#63d1db" />
                                                                    </g>
                                                                    <path id="Path_6537" data-name="Path 6537"
                                                                        d="M411.837,130.6a6.8,6.8,0,1,1-6.8-6.8A6.8,6.8,0,0,1,411.837,130.6Z"
                                                                        transform="translate(-286.532 -92.797)"
                                                                        fill="#f95632" />
                                                                    <path id="Path_6538" data-name="Path 6538"
                                                                        d="M82.708,82.646a7.76,7.76,0,1,1,7.759-7.759A7.768,7.768,0,0,1,82.708,82.646Zm0-12.83a5.07,5.07,0,1,0,5.069,5.071A5.075,5.075,0,0,0,82.708,69.816Z"
                                                                        transform="translate(-44.208 -50.315)"
                                                                        fill="#2e3133" />
                                                                    <path id="Path_6539" data-name="Path 6539"
                                                                        d="M178.924,66.8a2.749,2.749,0,1,1-2.75-2.748A2.75,2.75,0,0,1,178.924,66.8Z"
                                                                        transform="translate(-118.022 -48.014)"
                                                                        fill="#c1ced4" />
                                                                    <path id="Path_6540" data-name="Path 6540"
                                                                        d="M90.578,387.019v0L84.7,392.9h79.9v-5.88Z"
                                                                        transform="translate(-51.514 -290.092)"
                                                                        fill="#bc452a" />
                                                                    <g id="Group_2244" data-name="Group 2244"
                                                                        transform="translate(33.182 59.506)">
                                                                        <path id="Path_6541" data-name="Path 6541"
                                                                            d="M430.161,239.247l-2.122,2.122-3.761-3.764v3.806l3.759,3.761,2.124-2.123Z"
                                                                            transform="translate(-339.233 -237.606)"
                                                                            fill="#23282a" />
                                                                        <path id="Path_6542" data-name="Path 6542"
                                                                            d="M84.7,323.071l5.882-5.882v-3.8L84.7,319.268Z"
                                                                            transform="translate(-84.696 -294.407)"
                                                                            fill="#23282a" />
                                                                    </g>
                                                                    <path id="Path_6543" data-name="Path 6543"
                                                                        d="M412.459,165.5a3.2,3.2,0,0,0-2.27-.941h-8.273a6.784,6.784,0,0,0,10.809,1.265A2.939,2.939,0,0,0,412.459,165.5Z"
                                                                        transform="translate(-289.289 -123.345)"
                                                                        fill="#bc452a" />
                                                                    <path id="Path_6544" data-name="Path 6544"
                                                                        d="M89.227,114.55a5.035,5.035,0,0,1-5.894,0H79.715a7.737,7.737,0,0,0,13.13,0Z"
                                                                        transform="translate(-47.78 -85.862)"
                                                                        fill="#12282c" />
                                                                    <path id="Path_6545" data-name="Path 6545"
                                                                        d="M314.871,110.748c.187.008.371.028.56.028a12.15,12.15,0,0,0,7.615-2.675l-4.275-5.414a5.279,5.279,0,0,1-3.322,1.184c-.1,0-.189-.007-.29-.012Z"
                                                                        transform="translate(-224.044 -76.97)"
                                                                        fill="#bc452a" />
                                                                    <path id="Path_6546" data-name="Path 6546"
                                                                        d="M338.239,66.342l-6.295,2.816a5.3,5.3,0,0,1-1.507,6.284l4.346,5.363a12.172,12.172,0,0,0,3.456-14.464Z"
                                                                        transform="translate(-235.712 -49.727)"
                                                                        fill="#4ba9b6" />
                                                                    <path id="Path_6547" data-name="Path 6547"
                                                                        d="M290.011,37.9a12.186,12.186,0,0,0-2.666-.355l-1.265,9.536-7.255-6.313a12.254,12.254,0,0,0-1.772,2.023l11.036,9.6Z"
                                                                        transform="translate(-195.696 -28.146)"
                                                                        fill="#23282a" />
                                                                </g>
                                                            </svg>
                                                        </div>
                                                        <div className="categories-content">
                                                            <h4>Development</h4>
                                                            <p>22 Course available</p>
                                                        </div>
                                                    </div>
                                                </a></Link>
                                            </SwiperSlide>
                                            <SwiperSlide>
                                                <Link href="/course"><a>
                                                    <div className="categories-wrapper text-center mb-30">
                                                        <div className="categiories-thumb">
                                                            <svg xmlns="http://www.w3.org/2000/svg" width="125.018"
                                                                height="128.228" viewBox="0 0 125.018 128.228">
                                                                <g id="_x31_51_x2C__Flask_x2C__Data_x2C__Data_Science_x2C__Science_x2C__System"
                                                                    transform="translate(-45.001 -39.48)">
                                                                    <g id="Group_2254" data-name="Group 2254"
                                                                        transform="translate(45.001 39.48)">
                                                                        <path id="Path_6554" data-name="Path 6554"
                                                                            d="M217.8,39.48l-8.722,10.176H188.721L180,39.48Z"
                                                                            transform="translate(-140.749 -39.48)"
                                                                            fill="#95c9f4" />
                                                                        <path id="Path_6555" data-name="Path 6555"
                                                                            d="M125.037,107.947,141.6,131.471l1.756,2.368-14.2,20.7-4.334,30.425h-51.7a11.585,11.585,0,0,1-9.5-18.218l41.064-58.8V74.48h20.352Z"
                                                                            transform="translate(-56.713 -64.304)"
                                                                            fill="#dbebff" />
                                                                        <path id="Path_6556" data-name="Path 6556"
                                                                            d="M385.814,69.48A5.815,5.815,0,1,1,380,75.295,5.813,5.813,0,0,1,385.814,69.48Z"
                                                                            transform="translate(-282.601 -60.758)"
                                                                            fill="#b9dd39" />
                                                                        <circle id="Ellipse_36" data-name="Ellipse 36"
                                                                            cx="8.492" cy="8.492" r="8.492"
                                                                            transform="translate(1.698 42.46)"
                                                                            fill="#b9dd39" />
                                                                        <g id="Group_2251" data-name="Group 2251"
                                                                            transform="translate(55.24 58.448)">
                                                                            <path id="Path_6557" data-name="Path 6557"
                                                                                d="M266.282,266.439l7.977,5.7a25.9,25.9,0,0,0-7.644,7.644l-5.7-7.978Z"
                                                                                transform="translate(-253.382 -258.9)"
                                                                                fill="#554fb2" />
                                                                            <path id="Path_6558" data-name="Path 6558"
                                                                                d="M235,343.541l9.67-1.611a25.964,25.964,0,0,0,0,10.807L235,351.126Z"
                                                                                transform="translate(-234.999 -312.443)"
                                                                                fill="#554fb2" />
                                                                            <path id="Path_6559" data-name="Path 6559"
                                                                                d="M274.261,416.351l-7.934,5.667h-.076l-5.332-5.329,5.7-7.978h0A25.932,25.932,0,0,0,274.261,416.351Z"
                                                                                transform="translate(-253.383 -359.808)"
                                                                                fill="#554fb2" />
                                                                            <path id="Path_6560" data-name="Path 6560"
                                                                                d="M336.761,449.358h0l-.349-2.1a26.039,26.039,0,0,0,10.81,0l-1.614,9.67h-7.583Z"
                                                                                transform="translate(-306.925 -387.148)"
                                                                                fill="#554fb2" />
                                                                            <path id="Path_6561" data-name="Path 6561"
                                                                                d="M410.835,408.71l5.7,7.978-5.364,5.361-7.978-5.7a25.935,25.935,0,0,0,7.64-7.64Z"
                                                                                transform="translate(-354.291 -359.806)"
                                                                                fill="#554fb2" />
                                                                            <path id="Path_6562" data-name="Path 6562"
                                                                                d="M451.409,343.541v7.585l-9.667,1.61a25.96,25.96,0,0,0,0-10.807Z"
                                                                                transform="translate(-381.631 -312.443)"
                                                                                fill="#554fb2" />
                                                                            <path id="Path_6563" data-name="Path 6563"
                                                                                d="M416.53,271.8l-5.7,7.978a25.889,25.889,0,0,0-7.644-7.644l7.978-5.7Z"
                                                                                transform="translate(-354.288 -258.9)"
                                                                                fill="#554fb2" />
                                                                            <path id="Path_6564" data-name="Path 6564"
                                                                                d="M338.023,240.513h7.583l1.614,9.67a25.92,25.92,0,0,0-10.81,0l1.614-9.67Z"
                                                                                transform="translate(-306.925 -240.51)"
                                                                                fill="#554fb2" />
                                                                        </g>
                                                                        <path id="Path_6565" data-name="Path 6565"
                                                                            d="M317.848,297.609a25.836,25.836,0,0,1-.567,5.4h0a25.791,25.791,0,0,1-50.438,0h0a25.791,25.791,0,1,1,51.011-5.4Zm-11.252,0a14.537,14.537,0,1,0-14.537,14.537A14.537,14.537,0,0,0,306.6,297.609Z"
                                                                            transform="translate(-201.929 -204.27)"
                                                                            fill="#928bff" />
                                                                        <path id="Path_6566" data-name="Path 6566"
                                                                            d="M418.721,185.121a2.179,2.179,0,1,1-3.082,0A2.178,2.178,0,0,1,418.721,185.121Z"
                                                                            transform="translate(-307.426 -142.324)"
                                                                            fill="#95c9f4" />
                                                                        <path id="Path_6567" data-name="Path 6567"
                                                                            d="M318.721,105.121a2.179,2.179,0,1,1-3.082,0A2.178,2.178,0,0,1,318.721,105.121Z"
                                                                            transform="translate(-236.5 -85.584)"
                                                                            fill="#95c9f4" />
                                                                        <path id="Path_6568" data-name="Path 6568"
                                                                            d="M143.722,125.121a2.179,2.179,0,1,1-3.082,0A2.178,2.178,0,0,1,143.722,125.121Z"
                                                                            transform="translate(-112.38 -99.769)"
                                                                            fill="#95c9f4" />
                                                                        <path id="Path_6569" data-name="Path 6569"
                                                                            d="M55.64,55.121a2.179,2.179,0,1,1,0,3.082,2.178,2.178,0,0,1,0-3.082Z"
                                                                            transform="translate(-52.094 -50.121)"
                                                                            fill="#95c9f4" />
                                                                        <path id="Path_6570" data-name="Path 6570"
                                                                            d="M48.722,310.121a2.179,2.179,0,1,1-3.082,0A2.178,2.178,0,0,1,48.722,310.121Z"
                                                                            transform="translate(-45.001 -230.981)"
                                                                            fill="#95c9f4" />
                                                                        <path id="Path_6571" data-name="Path 6571"
                                                                            d="M117.345,369.484H90.14a4.607,4.607,0,0,1-3.777-7.246L98.333,345.1a1.454,1.454,0,1,1,2.384,1.665L88.747,363.9a1.7,1.7,0,0,0,1.393,2.674h27.2a1.454,1.454,0,1,1,0,2.907Z"
                                                                            transform="translate(-73.734 -255.804)"
                                                                            fill="#95c9f4" />
                                                                        <path id="Path_6572" data-name="Path 6572"
                                                                            d="M160.76,276.876a1.454,1.454,0,0,1-.36-2.024l7.228-10.35a1.454,1.454,0,0,1,2.384,1.664l-7.228,10.35A1.454,1.454,0,0,1,160.76,276.876Z"
                                                                            transform="translate(-126.663 -198.637)"
                                                                            fill="#95c9f4" />
                                                                        <g id="Group_2252" data-name="Group 2252"
                                                                            transform="translate(47.972 15.991)">
                                                                            <path id="Path_6573" data-name="Path 6573"
                                                                                d="M215.814,97.387a1.454,1.454,0,1,0,0-2.907H210v2.907Z"
                                                                                transform="translate(-209.999 -94.48)"
                                                                                fill="#95c9f4" />
                                                                            <path id="Path_6574" data-name="Path 6574"
                                                                                d="M215.814,122.387a1.454,1.454,0,1,0,0-2.907H210v2.907Z"
                                                                                transform="translate(-209.999 -112.211)"
                                                                                fill="#95c9f4" />
                                                                            <path id="Path_6575" data-name="Path 6575"
                                                                                d="M215.814,147.387a1.454,1.454,0,1,0,0-2.907H210v2.907Z"
                                                                                transform="translate(-209.999 -129.943)"
                                                                                fill="#95c9f4" />
                                                                        </g>
                                                                        <g id="Group_2253" data-name="Group 2253"
                                                                            transform="translate(61.055 15.991)">
                                                                            <path id="Path_6576" data-name="Path 6576"
                                                                                d="M256.453,94.48a1.454,1.454,0,0,0,0,2.907h5.815V94.48Z"
                                                                                transform="translate(-254.999 -94.48)"
                                                                                fill="#95c9f4" />
                                                                            <path id="Path_6577" data-name="Path 6577"
                                                                                d="M256.453,119.48a1.454,1.454,0,1,0,0,2.907h5.815V119.48Z"
                                                                                transform="translate(-254.999 -112.211)"
                                                                                fill="#d5e8fe" />
                                                                            <path id="Path_6578" data-name="Path 6578"
                                                                                d="M256.453,144.48a1.454,1.454,0,0,0,0,2.907h5.815V144.48Z"
                                                                                transform="translate(-254.999 -129.943)"
                                                                                fill="#95c9f4" />
                                                                        </g>
                                                                    </g>
                                                                </g>
                                                            </svg>
                                                        </div>
                                                        <div className="categories-content">
                                                            <h4>Data Science</h4>
                                                            <p>8 Course available</p>
                                                        </div>
                                                    </div>
                                                </a></Link>
                                            </SwiperSlide>
                                            <SwiperSlide>
                                                <Link href="/course"><a>
                                                    <div className="categories-wrapper text-center mb-30">
                                                        <div className="categiories-thumb">
                                                            <svg id="Camera" xmlns="http://www.w3.org/2000/svg"
                                                                width="128.228" height="128.228"
                                                                viewBox="0 0 128.228 128.228">
                                                                <g id="Group_2255" data-name="Group 2255"
                                                                    transform="translate(30.053 80.142)">
                                                                    <path id="Path_6579" data-name="Path 6579"
                                                                        d="M17,88.086a2,2,0,0,1-1.723-3.023L41.325,40.984a2,2,0,1,1,3.449,2.039L18.728,87.1A2,2,0,0,1,17,88.086Z"
                                                                        transform="translate(-15 -40)" fill="#2946ed" />
                                                                    <path id="Path_6580" data-name="Path 6580"
                                                                        d="M62.052,88.086a2,2,0,0,1-1.727-.984L34.279,43.023a2,2,0,1,1,3.449-2.039L63.775,85.063a2,2,0,0,1-1.723,3.023Z"
                                                                        transform="translate(4.068 -40)"
                                                                        fill="#2946ed" />
                                                                    <path id="Path_6581" data-name="Path 6581"
                                                                        d="M33,86.082a2,2,0,0,1-2-2V42a2,2,0,0,1,4.007,0V84.078A2,2,0,0,1,33,86.082Z"
                                                                        transform="translate(1.057 -40)"
                                                                        fill="#2946ed" />
                                                                </g>
                                                                <rect id="Rectangle_120" data-name="Rectangle 120"
                                                                    width="16" height="12" rx="1"
                                                                    transform="translate(56 72.169)" fill="#376cfb" />
                                                                <rect id="Rectangle_121" data-name="Rectangle 121"
                                                                    width="24" height="12" rx="1"
                                                                    transform="translate(52 64.169)" fill="#4294ff" />
                                                                <path id="Path_6582" data-name="Path 6582"
                                                                    d="M58.975,9.532l-2-8.014A2,2,0,0,0,55.028,0H43.007a2,2,0,0,0-1.943,1.517l-2,8.014A2,2,0,0,0,41,12.021H57.032a2,2,0,0,0,1.943-2.49Z"
                                                                    transform="translate(39.139)" fill="#4294ff" />
                                                                <rect id="Rectangle_122" data-name="Rectangle 122"
                                                                    width="96" height="60" rx="5"
                                                                    transform="translate(16 8.169)" fill="#e6ecff" />
                                                                <path id="Path_6583" data-name="Path 6583"
                                                                    d="M94.153,4H18.018A10.03,10.03,0,0,0,8,14.018V24.036h96.171V14.018A10.03,10.03,0,0,0,94.153,4Z"
                                                                    transform="translate(8.029 4.014)" fill="#d0dbf7" />
                                                                <circle id="Ellipse_37" data-name="Ellipse 37" cx="20"
                                                                    cy="20" r="20" transform="translate(58 18.169)"
                                                                    fill="#4294ff" />
                                                                <circle id="Ellipse_38" data-name="Ellipse 38" cx="14"
                                                                    cy="14" r="14" transform="translate(64 24.169)"
                                                                    fill="#376cfb" />
                                                                <path id="Path_6584" data-name="Path 6584"
                                                                    d="M54.011,12.007H50A2,2,0,1,1,50,8h4.007a2,2,0,1,1,0,4.007Z"
                                                                    transform="translate(48.171 8.029)"
                                                                    fill="#4294ff" />
                                                                <path id="Path_6585" data-name="Path 6585"
                                                                    d="M126.225,66.007H2A2,2,0,1,1,2,62H126.225a2,2,0,0,1,0,4.007Z"
                                                                    transform="translate(0 62.221)" fill="#376cfb" />
                                                                <path id="Path_6586" data-name="Path 6586"
                                                                    d="M37,25.018a2,2,0,0,1-2-2A8.023,8.023,0,0,1,43.014,15a2,2,0,0,1,0,4.007,4.011,4.011,0,0,0-4.007,4.007A2,2,0,0,1,37,25.018Z"
                                                                    transform="translate(35.125 15.053)"
                                                                    fill="#2946ed" />
                                                            </svg>
                                                        </div>
                                                        <div className="categories-content">
                                                            <h4>Photography</h4>
                                                            <p>22 Course available</p>
                                                        </div>
                                                    </div>
                                                </a></Link>
                                            </SwiperSlide>
                                            <SwiperSlide>
                                                <Link href="/course"><a>
                                                    <div className="categories-wrapper text-center mb-30">
                                                        <div className="categiories-thumb">
                                                            <svg xmlns="http://www.w3.org/2000/svg" width="128.221"
                                                                height="128.228" viewBox="0 0 128.221 128.228">
                                                                <g id="Digital_Marketing" data-name="Digital Marketing"
                                                                    transform="translate(-0.974 -0.971)">
                                                                    <path id="Path_6587" data-name="Path 6587"
                                                                        d="M72.983,47.715a2.591,2.591,0,0,1-1.839-4.429,2.507,2.507,0,0,0,.78-1.811c0-1.421-.5-1.56-4.987-6.045a2.535,2.535,0,0,0-3.649,0,2.591,2.591,0,0,1-4.429-1.839A2.591,2.591,0,0,0,56.268,31H50.306a2.591,2.591,0,0,0-2.591,2.591,2.591,2.591,0,0,1-4.429,1.839,2.535,2.535,0,0,0-1.811-.78c-1.421,0-1.56.5-6.045,4.987a2.535,2.535,0,0,0,0,3.649,2.591,2.591,0,0,1-1.839,4.429A2.591,2.591,0,0,0,31,50.306v5.962a2.591,2.591,0,0,0,2.591,2.591,2.591,2.591,0,0,1,1.839,4.43,2.507,2.507,0,0,0,0,3.649l4.207,4.207a2.535,2.535,0,0,0,3.649,0,2.591,2.591,0,0,1,4.43,1.839,2.591,2.591,0,0,0,2.591,2.591h5.962a2.591,2.591,0,0,0,2.591-2.591,2.591,2.591,0,0,1,2.619-2.591c1.867,0,1.811,1.532,3.622,1.532s1.56-.5,6.045-4.987a2.563,2.563,0,0,0,0-3.649,2.591,2.591,0,0,1,1.839-4.429,2.591,2.591,0,0,0,2.591-2.591V50.306a2.591,2.591,0,0,0-2.591-2.591Zm-24.042-1.56a8.358,8.358,0,1,1-3.689,9.389,8.357,8.357,0,0,1,3.689-9.389Z"
                                                                        transform="translate(53.622 53.626)"
                                                                        fill="#374f68" />
                                                                    <path id="Path_6588" data-name="Path 6588"
                                                                        d="M67.69,47.715a2.591,2.591,0,0,1-1.839-4.429,2.507,2.507,0,0,0,.78-1.811c0-1.421-.5-1.56-4.987-6.045a2.535,2.535,0,0,0-3.649,0,2.591,2.591,0,0,1-4.429-1.839A2.591,2.591,0,0,0,50.975,31H45.013a2.591,2.591,0,0,0-2.591,2.591,2.591,2.591,0,0,1-4.429,1.839,2.256,2.256,0,0,0-1.421-.724c-10.057,19.055,10.893,40.088,30,30-.167-1.5-1.476-1.476-1.476-3.232a2.591,2.591,0,0,1,2.591-2.619,2.591,2.591,0,0,0,2.591-2.591V50.306a2.591,2.591,0,0,0-2.591-2.591ZM55.126,57.633a8.358,8.358,0,1,1,.149-8.437A8.358,8.358,0,0,1,55.126,57.633Z"
                                                                        transform="translate(58.914 53.626)"
                                                                        fill="#425b72" />
                                                                    <rect id="Rectangle_123" data-name="Rectangle 123"
                                                                        width="44.573" height="33.43" rx="2"
                                                                        transform="translate(84.622 1.051)"
                                                                        fill="#db5669" />
                                                                    <path id="Path_6589" data-name="Path 6589"
                                                                        d="M70.216,6.572V28.858H39.572A5.572,5.572,0,0,1,34,23.287V1H64.644A5.572,5.572,0,0,1,70.216,6.572Z"
                                                                        transform="translate(58.979 0.051)"
                                                                        fill="#f26674" />
                                                                    <path id="Path_6590" data-name="Path 6590"
                                                                        d="M45.62,27.948a5.572,5.572,0,0,1-5.572,5.572H6.618a5.572,5.572,0,0,1-5.572-5.572C1.046,4.3.406,3.683,2.941,1.51L23.333,16.8,43.725,1.51C46.261,3.683,45.62,3.934,45.62,27.948Z"
                                                                        transform="translate(0 0.962)" fill="#fc6" />
                                                                    <path id="Path_6591" data-name="Path 6591"
                                                                        d="M40.216,27.948H9.572A5.572,5.572,0,0,1,4,22.376V6.357L17.929,16.8,38.322,1.51C40.857,3.683,40.216,3.934,40.216,27.948Z"
                                                                        transform="translate(5.404 0.962)"
                                                                        fill="#ffde76" />
                                                                    <path id="Path_6592" data-name="Path 6592"
                                                                        d="M48.728,13.73,41.04,17.6A2.786,2.786,0,0,1,37,15.095V7.406A2.786,2.786,0,0,1,39.786,4.62c.919,0,.585,0,8.859,4.179a2.786,2.786,0,0,1,.084,4.931Z"
                                                                        transform="translate(64.337 6.516)"
                                                                        fill="#dad7e5" />
                                                                    <path id="Path_6593" data-name="Path 6593"
                                                                        d="M42.465,2.463,22.072,17.757,1.68,2.463A5.4,5.4,0,0,1,5.357,1.042c35.687,0,34.962-.529,37.107,1.421Z"
                                                                        transform="translate(1.261 0.009)"
                                                                        fill="#ffba55" />
                                                                    <path id="Path_6594" data-name="Path 6594"
                                                                        d="M36.949,50.556,25.276,62.368a8.358,8.358,0,0,1-11.812,0L5.441,54.484A8.358,8.358,0,0,1,4.131,44.4c.724-1.309.613-1.031,13.121-13.539Z"
                                                                        transform="translate(3.629 53.376)"
                                                                        fill="#db5669" />
                                                                    <path id="Path_6595" data-name="Path 6595"
                                                                        d="M31.959,46.377,24.465,54.01a8.358,8.358,0,0,1-11.812,0C4.1,45.458,4.129,45.653,3.46,44.4c.724-1.309.613-1.031,13.121-13.539Z"
                                                                        transform="translate(4.439 53.376)"
                                                                        fill="#f26674" />
                                                                    <path id="Path_6596" data-name="Path 6596"
                                                                        d="M22.673,59.036,11,47.224l7.884-7.884C31.615,52.322,30.974,51.486,31.42,52.322a5.572,5.572,0,0,1-8.748,6.714Z"
                                                                        transform="translate(17.905 68.52)"
                                                                        fill="#374f68" />
                                                                    <path id="Path_6597" data-name="Path 6597"
                                                                        d="M29.634,52.322a5.572,5.572,0,0,1-8.748,1.142L12,44.438l5.1-5.1C29.829,52.322,29.189,51.486,29.634,52.322Z"
                                                                        transform="translate(19.691 68.52)"
                                                                        fill="#425b72" />
                                                                    <path id="Path_6598" data-name="Path 6598"
                                                                        d="M65.258,56.812,27.816,74.558l-19.7-19.7C12.1,46.5,8.649,53.8,25.866,17.42Z"
                                                                        transform="translate(12.762 29.374)"
                                                                        fill="#dad7e5" />
                                                                    <path id="Path_6599" data-name="Path 6599"
                                                                        d="M57.842,51.129,31.4,63.609a8.358,8.358,0,0,1-9.472-1.644L9.09,49.179,24.134,17.42Z"
                                                                        transform="translate(14.494 29.374)"
                                                                        fill="#edebf2" />
                                                                    <path id="Path_6600" data-name="Path 6600"
                                                                        d="M37.757,31.649a11.143,11.143,0,0,1-3.287,7.856L18.73,23.765a11.143,11.143,0,0,1,19.027,7.884Z"
                                                                        transform="translate(31.709 34.869)"
                                                                        fill="#374f68" />
                                                                    <path id="Path_6601" data-name="Path 6601"
                                                                        d="M35.541,30.6a5.572,5.572,0,0,1-7.076-.613L19.94,21.46a11.143,11.143,0,0,1,15.6,9.138Z"
                                                                        transform="translate(33.87 34.889)"
                                                                        fill="#425b72" />
                                                                    <g id="Group_2256" data-name="Group 2256"
                                                                        transform="translate(59.549 34.481)">
                                                                        <path id="Path_6602" data-name="Path 6602"
                                                                            d="M28.988,27.013a2.786,2.786,0,0,1-1.95-4.764l5.906-5.934a2.786,2.786,0,0,1,3.817,3.956C30.465,26.707,30.521,27.013,28.988,27.013Z"
                                                                            transform="translate(-14.474 -7.958)"
                                                                            fill="#f26674" />
                                                                        <path id="Path_6603" data-name="Path 6603"
                                                                            d="M22,24.143V15.786a2.786,2.786,0,1,1,5.572,0v8.358a2.786,2.786,0,0,1-5.572,0Z"
                                                                            transform="translate(-22 -13)"
                                                                            fill="#f26674" />
                                                                        <path id="Path_6604" data-name="Path 6604"
                                                                            d="M39.143,27.572H30.786a2.786,2.786,0,0,1,0-5.572h8.358a2.786,2.786,0,1,1,0,5.572Z"
                                                                            transform="translate(-11.285 3.073)"
                                                                            fill="#f26674" />
                                                                    </g>
                                                                    <path id="Path_6605" data-name="Path 6605"
                                                                        d="M46.4,14.054A8.358,8.358,0,0,1,38.046,4.61c.919,0,.585,0,8.859,4.179,1.56.724,2.925,3.454-.5,5.265Z"
                                                                        transform="translate(66.077 6.498)"
                                                                        fill="#edebf2" />
                                                                    <path id="Path_6606" data-name="Path 6606"
                                                                        d="M38.322,2.472,17.929,17.766,4,7.319V1.051C36.371,1.051,36.121.466,38.322,2.472Z"
                                                                        transform="translate(5.404 0)" fill="#fc6" />
                                                                </g>
                                                            </svg>
                                                        </div>
                                                        <div className="categories-content">
                                                            <h4>Marketing</h4>
                                                            <p>14 Course available</p>
                                                        </div>
                                                    </div>
                                                </a></Link>
                                            </SwiperSlide>
                                            </Swiper>
                                        </div>
                                    </div>

                                    <div className="category-nav">
                                        <div className="category-button-prev"><i className="far fa-long-arrow-left"></i></div>
                                        <div className="category-button-next"><i className="far fa-long-arrow-right"></i></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default CategorySlider;