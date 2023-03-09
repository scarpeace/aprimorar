import React from 'react';
import { Swiper, SwiperSlide } from 'swiper/react';
import { Navigation, Pagination, Scrollbar, A11y, Autoplay, } from 'swiper';

// Import Swiper styles
import 'swiper/css/bundle'

const BrandSlider = () => {
    return (
        <div className="brand-area theme-bg pt-90 pb-120">
         <div className="container">
            <div className="row">
               <div className="col-xl-12">
                  <div className="brand-wrapper text-center">
                     <div className="brand-wrapper text-center">
                        <div className="swiper-container brand-active">
                           <div className="swiper-wrapper">
                           <Swiper
                                modules={[Navigation, Pagination, Scrollbar, A11y,Autoplay]}
                                spaceBetween={30}
                                slidesPerView={2}
                                autoplaydisableoninteraction={"false"}
                                loop={true}
                                breakpoints={{
                                    320: {
                                        slidesPerView: 1,
                                      },
                                      480: {
                                       slidesPerView: 2,
                                     },
                                      640: {
                                        slidesPerView: 3,
                                      },
                                      991: {
                                       slidesPerView: 4,
                                     },
                                      1200: {
                                        slidesPerView: 5,
                                      },
                                      1400: {
                                       slidesPerView: 6,
                                     }
                                }}
                                autoplay= {{
                                    delay: 2000,
                                    disableOnInteraction: true
                                }}
                              //  onSwiper={(swiper) => console.log(swiper)}
                              //  onSlideChange={() => console.log('slide change')}
                            >
                              <SwiperSlide>
                                 <div className="singel-brand">
                                    <img src="assets/img/brand/brand-01.png" alt="image not found"/>
                                 </div>
                              </SwiperSlide>

                              <SwiperSlide>
                                 <div className="singel-brand">
                                    <img src="assets/img/brand/brand-02.png" alt="image not found"/>
                                 </div>
                              </SwiperSlide>

                              <SwiperSlide>
                                 <div className="singel-brand">
                                    <img src="assets/img/brand/brand-03.png" alt="image not found"/>
                                 </div>
                              </SwiperSlide>

                              <SwiperSlide>
                                 <div className="singel-brand">
                                    <img src="assets/img/brand/brand-04.png" alt="image not found"/>
                                 </div>
                              </SwiperSlide>

                              <SwiperSlide>
                                 <div className="singel-brand">
                                    <img src="assets/img/brand/brand-05.png" alt="image not found"/>
                                 </div>
                              </SwiperSlide>

                              <SwiperSlide>
                                 <div className="singel-brand">
                                    <img src="assets/img/brand/brand-06.png" alt="image not found"/>
                                 </div>
                              </SwiperSlide>
                              </Swiper>
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

export default BrandSlider;