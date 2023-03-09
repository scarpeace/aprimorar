import React from 'react';
import Link from 'next/link';

const BreadcrumbTwo = ({ breadcrumbTitleTwo, breadcrumbSubTitleTwo }) => {
    return (
        <div className="banner-area faq position-relative">
         <div className="banner-img">
            <img src="assets/img/banner/banner.png" alt="image not found"/>
         </div>
         <div className="container">
            <div className="row justify-content-center">
               <div className="course-title-breadcrumb breadcrumb-top">
                  <nav>
                     <ol className="breadcrumb">
                        <li className="breadcrumb-item white-color"><Link href="/"><a>Home</a></Link></li>
                        <li className="breadcrumb-item white-color"><Link href="/faq-page"><a>{breadcrumbSubTitleTwo}</a></Link></li>
                     </ol>
                  </nav>
               </div>
               <div className="col-xl-8">
                  <div className="banner-tiitle-wrapper text-center">
                     <div className="banner-tittle">
                        <h2>{breadcrumbTitleTwo}</h2>
                     </div>
                     <div className="banner-search-box">
                        <form action="#">
                           <div className="slider-faq-search">
                              <input type="text" placeholder="Search courses..."/>
                              <button type="submit"><i className="fal fa-search"></i></button>
                           </div>
                        </form>
                     </div>
                  </div>
               </div>
            </div>
         </div>
      </div>
    );
};

export default BreadcrumbTwo;