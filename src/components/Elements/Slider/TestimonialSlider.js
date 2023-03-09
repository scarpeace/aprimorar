import React from 'react';
import { Swiper, SwiperSlide } from 'swiper/react';
import { Navigation, Pagination, Scrollbar, A11y, Autoplay, } from 'swiper';

// Import Swiper styles
import 'swiper/css/bundle'

const TestimonialSlider = () => {
    return (
        <div className="testimonial-area pb-120">
         <div className="container">
            <div className="row">
               <div className="col-lg-6 offset-lg-3">
                  <div className="section-title text-center mb-55">
                     <h2>What Students<br/>
                        Think and Say About <span className="down-mark-line">Eduman</span></h2>
                  </div>
               </div>
            </div>
            <div className="swiper-container testimonial-active">
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
                                slidesPerView: 1
                            },
                            991: {
                                slidesPerView: 2
                            },
                            1200: {
                                slidesPerView: 3
                            },
                            1400: {
                                slidesPerView: 3
                            }
                        }}
                        pagination={{
                            clickable: true,
                            el: '.testimonial-pagination',
                       }}
                        autoplay= {{
                            delay: 2000,
                            disableOnInteraction: true
                        }}
                        // onSwiper={(swiper) => console.log(swiper)}
                        // onSlideChange={() => console.log('slide change')}
                    >
                  <SwiperSlide>
                     <div className="testimonial-items position-relative">
                        <div className="testimonial-header">
                           <div className="testimonial-img">
                              <img src="assets/img/testimonial/Image.png" alt="image not found"/>
                           </div>
                           <div className="testimonial-title">
                              <h4>David Johnson</h4>
                              <span>Student</span>
                           </div>
                        </div>
                        <div className="testimoni-quotes">
                           <img src="assets/img/testimonial/quotes.png" alt="image not found"/>

                        </div>
                        <div className="testimonial-body">
                           <h3>Great Course !</h3>
                           <p>Thanks to our marketplace model, our content keeps pace with market changes. You’ll find
                              courses on the latest technologies and business practice and more!</p>
                        </div>
                        <div className="testimonial-icon">
                           <i className="fas fa-star"></i>
                           <i className="fas fa-star"></i>
                           <i className="fas fa-star"></i>
                           <i className="fas fa-star"></i>
                        </div>
                     </div>
                  </SwiperSlide>

                  <SwiperSlide>
                     <div className="testimonial-items position-relative">
                        <div className="testimonial-header">
                           <div className="testimonial-img">
                              <img src="assets/img/testimonial/testimonial-02.png" alt="image not found"/>
                           </div>
                           <div className="testimonial-title">
                              <h4>Brandon Tylor</h4>
                              <span>Student</span>
                           </div>
                        </div>
                        <div className="testimoni-quotes">
                           <img src="assets/img/testimonial/quotes.png" alt="image not found"/>
                        </div>
                        <div className="testimonial-body">
                           <h3>Best Experience !</h3>
                           <p>In every software-as-a-service solution, user billing and payments are key aspects in the
                              sale of services rendered. Let’s learn about Stripe the metal mates.</p>
                        </div>
                        <div className="testimonial-icon">
                           <i className="fas fa-star"></i>
                           <i className="fas fa-star"></i>
                           <i className="fas fa-star"></i>
                           <i className="fas fa-star"></i>
                        </div>
                     </div>
                  </SwiperSlide>

                  <SwiperSlide>
                     <div className="testimonial-items position-relative">
                        <div className="testimonial-header">
                           <div className="testimonial-img">
                              <img src="assets/img/testimonial/testimonial.png" alt="image not found"/>
                           </div>
                           <div className="testimonial-title">
                              <h4>Richard Joseph</h4>
                              <span>Student</span>
                           </div>
                        </div>
                        <div className="testimoni-quotes">
                           <img src="assets/img/testimonial/quotes.png" alt="image not found"/>
                        </div>
                        <div className="testimonial-body">
                           <h3>Helpful Instructors !</h3>
                           <p>There are so many websites out there that have not considered the overall usability of
                              their visually impaired users. When it comes to designing better links.</p>
                        </div>
                        <div className="testimonial-icon">
                           <i className="fas fa-star"></i>
                           <i className="fas fa-star"></i>
                           <i className="fas fa-star"></i>
                           <i className="fas fa-star"></i>
                        </div>
                     </div>
                  </SwiperSlide>
                  </Swiper>
               </div>
               
               <div className="testimonial-pagination text-center"></div>
            </div>
         </div>
      </div>
    );
};

export default TestimonialSlider;