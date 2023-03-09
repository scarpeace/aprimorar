import React from 'react';
import { Navigation, Pagination, Scrollbar, A11y, Autoplay, } from 'swiper';
import { Swiper, SwiperSlide } from 'swiper/react';
import Link from 'next/link';

// Import Swiper styles
import 'swiper/css/bundle';

const RelatedProductSlider = () => {
    return (
        <div className="related_product pb-115">
            <div className="container custome-container">
                <div className="section-title mb-55">
                    <h2>Related Product</h2>
                </div>
                <div className="swiper-container r-product-active">
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
                                    slidesPerView: 2
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
                        // pagination={{ clickable: true }}
                        // scrollbar={{ draggable: true }}
                        // onSwiper={(swiper) => console.log(swiper)}
                        // onSlideChange={() => console.log("slide change")}
                        >
                        
                        <SwiperSlide>
                            <div className="product-items text-center">
                                <div className="product-img">
                                    <Link href="/shop-details"><a><img src="assets/img/products/product-thumb-01.png" alt="product-img"/></a></Link>
                                    <div className="shop-quick-view">
                                        <ul>
                                            <li>
                                                <Link href="/cart"><a><i className="fal fa-cart-arrow-down"></i></a></Link>
                                            </li>
                                            <li>
                                                <Link href="/wishlist"><a><i className="fal fa-heart"></i></a></Link>
                                            </li>
                                            <li>
                                                <Link href="/cart"><a><i className="fal fa-eye"></i></a></Link>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                                <div className="product-content">
                                    <h4><Link href="/shop-details"><a>Turn Yourself</a></Link></h4>
                                    <span>$24.00</span>
                                    <div className="course-icon">
                                        <i className="fas fa-star"></i>
                                        <i className="fas fa-star"></i>
                                        <i className="fas fa-star"></i>
                                        <i className="fas fa-star"></i>
                                        <i className="fal fa-star"></i>
                                    </div>
                                </div>
                            </div>
                        </SwiperSlide>
                        <SwiperSlide>
                            <div className="product-items text-center">
                                <div className="product-img">
                                    <Link href="/shop-details"><a><img src="assets/img/products/product-thumb-02.png" alt="product-img"/></a></Link>
                                    <div className="shop-quick-view">
                                        <ul>
                                            <li>
                                                <Link href="/cart"><a><i className="fal fa-cart-arrow-down"></i></a></Link>
                                            </li>
                                            <li>
                                                <Link href="/wishlist"><a><i className="fal fa-heart"></i></a></Link>
                                            </li>
                                            <li>
                                                <Link href="/cart"><a><i className="fal fa-eye"></i></a></Link>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                                <div className="product-content">
                                    <h4><Link href="/shop-details"><a>Art of Not Giving</a></Link></h4>
                                    <span>$24.00</span>
                                    <div className="course-icon">
                                        <i className="fas fa-star"></i>
                                        <i className="fas fa-star"></i>
                                        <i className="fas fa-star"></i>
                                        <i className="fas fa-star"></i>
                                        <i className="fal fa-star"></i>
                                    </div>
                                </div>
                            </div>
                        </SwiperSlide>
                        <SwiperSlide>
                            <div className="product-items text-center">
                                <div className="product-img">
                                    <Link href="/shop-details"><a><img src="assets/img/products/product-thumb-03.png" alt="product-img"/></a></Link>
                                    <div className="shop-quick-view">
                                        <ul>
                                            <li>
                                                <Link href="/cart"><a><i className="fal fa-cart-arrow-down"></i></a></Link>
                                            </li>
                                            <li>
                                                <Link href="/wishlist"><a><i className="fal fa-heart"></i></a></Link>
                                            </li>
                                            <li>
                                                <Link href="/cart"><a><i className="fal fa-eye"></i></a></Link>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                                <div className="product-content">
                                    <h4><Link href="/shop-details"><a>Attract Women</a></Link></h4>
                                    <span>$24.00</span>
                                    <div className="course-icon">
                                        <i className="fas fa-star"></i>
                                        <i className="fas fa-star"></i>
                                        <i className="fas fa-star"></i>
                                        <i className="fas fa-star"></i>
                                        <i className="fal fa-star"></i>
                                    </div>
                                </div>
                            </div>
                        </SwiperSlide>
                        <SwiperSlide>
                            <div className="product-items text-center">
                                <div className="product-img">
                                    <Link href="/shop-details"><a><img src="assets/img/products/product-thumb-04.png" alt="product-img"/></a></Link>
                                    <div className="shop-quick-view">
                                        <ul>
                                            <li>
                                                <Link href="/cart"><a><i className="fal fa-cart-arrow-down"></i></a></Link>
                                            </li>
                                            <li>
                                                <Link href="/wishlist"><a><i className="fal fa-heart"></i></a></Link>
                                            </li>
                                            <li>
                                                <Link href="/cart"><a><i className="fal fa-eye"></i></a></Link>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                                <div className="product-content">
                                    <h4><Link href="/shop-details"><a>Think and Grow Rich</a></Link></h4>
                                    <span>$24.00</span>
                                    <div className="course-icon">
                                        <i className="fas fa-star"></i>
                                        <i className="fas fa-star"></i>
                                        <i className="fas fa-star"></i>
                                        <i className="fas fa-star"></i>
                                        <i className="fal fa-star"></i>
                                    </div>
                                </div>
                            </div>
                        </SwiperSlide>
                        <SwiperSlide>
                            <div className="product-items text-center">
                                <div className="product-img">
                                    <Link href="/shop-details"><a><img src="assets/img/products/product-thumb-05.png" alt="product-img"/></a></Link>
                                    <div className="shop-quick-view">
                                        <ul>
                                            <li>
                                                <Link href="/cart"><a><i className="fal fa-cart-arrow-down"></i></a></Link>
                                            </li>
                                            <li>
                                                <Link href="/wishlist"><a><i className="fal fa-heart"></i></a></Link>
                                            </li>
                                            <li>
                                                <Link href="/cart"><a><i className="fal fa-eye"></i></a></Link>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                                <div className="product-content">
                                    <h4><Link href="/shop-details"><a>Best Inspirational</a></Link></h4>
                                    <span>$24.00</span>
                                    <div className="course-icon">
                                        <i className="fas fa-star"></i>
                                        <i className="fas fa-star"></i>
                                        <i className="fas fa-star"></i>
                                        <i className="fas fa-star"></i>
                                        <i className="fal fa-star"></i>
                                    </div>
                                </div>
                            </div>
                        </SwiperSlide>
                        </Swiper>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default RelatedProductSlider;