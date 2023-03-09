import React from 'react';
import Link from 'next/link';

const MembershipPrice = () => {
    return (
        <section className="membership-area pt-110 pb-80">
         <div className="container">
            <div className="row justify-content-center">
               <div className="col-xl-6 col-lg-6">
                  <div className="section-title text-center mb-40">
                     <h2>Membership Plan</h2>
                     <p>Whether you're looking for smart course , start your next journey with course of
                        famous instructors we have a plan for you.</p>
                  </div>
                  <div className="pricing-tab mb-60">
                     <ul className="nav nav-tabs justify-content-center" id="priceTab" role="tablist">
                        <li className="nav-item" role="presentation">
                           <button className="nav-link active" id="monthly-tab" data-bs-toggle="tab"
                              data-bs-target="#monthly" type="button" role="tab" aria-controls="monthly"
                              aria-selected="true">monthly</button>
                        </li>
                        <li className="nav-item" role="presentation">
                           <button className="nav-link" id="yearly-tab" data-bs-toggle="tab" data-bs-target="#yearly"
                              type="button" role="tab" aria-controls="yearly" aria-selected="false">yearly</button>
                        </li>
                     </ul>
                  </div>
               </div>
               <div className="tab-content" id="priceTabContent">
                  <div className="tab-pane fade active show" id="monthly" role="tabpanel" aria-labelledby="monthly-tab">
                     <div className="row">
                        <div className="col-xl-3 col-lg-6 col-md-6">
                           <div className="membership-box mb-30">
                              <div className="membership-info">
                                 <h4>Basic</h4>
                                 <div className="membership-price">
                                    <span>Free</span>
                                    <p>Free for starters</p>
                                 </div>
                                 <div className="membership-list">
                                    <ul>
                                       <li><i className="far fa-check"></i>Up to 2 Devices</li>
                                       <li><i className="far fa-check"></i>Handle Basic Option</li>
                                       <li><i className="far fa-check"></i>Individual Course</li>
                                       <li><i className="far fa-check"></i>Free for starters</li>
                                    </ul>
                                 </div>
                              </div>
                              <Link href="/contact"><a className="membership-btn">Choose plan</a></Link>
                           </div>
                        </div>
                        <div className="col-xl-3 col-lg-6 col-md-6">
                           <div className="membership-box  mb-30">
                              <div className="membership-info">
                                 <h4>Basic</h4>
                                 <div className="membership-price">
                                    <span>9.00</span>
                                    <p>Free for starters</p>
                                 </div>
                                 <div className="membership-list">
                                    <ul>
                                       <li><i className="far fa-check"></i>Up to 5 Devices</li>
                                       <li><i className="far fa-check"></i>Handle Basic Option</li>
                                       <li><i className="far fa-check"></i>Individual Course</li>
                                       <li><i className="far fa-check"></i>3 Times Access</li>
                                    </ul>
                                 </div>
                              </div>
                              <Link href="/contact"><a className="membership-btn">Choose plan</a></Link>
                           </div>
                        </div>
                        <div className="col-xl-3 col-lg-6 col-md-6">
                           <div className="membership-box mb-30">
                              <div className="membership-info">
                                 <h4>Basic</h4>
                                 <div className="membership-price">
                                    <span>14.00</span>
                                    <p>Free for starters</p>
                                 </div>
                                 <div className="membership-list">
                                    <ul>
                                       <li><i className="far fa-check"></i>Up to 10 Devices</li>
                                       <li><i className="far fa-check"></i>Handle Basic Option</li>
                                       <li><i className="far fa-check"></i>Individual Course</li>
                                       <li><i className="far fa-check"></i>Full Time Access</li>
                                       <li><i className="far fa-check"></i>Monthly Backup</li>
                                    </ul>
                                 </div>
                              </div>
                              <Link href="/contact"><a className="membership-btn">Choose plan</a></Link>
                              <div className="Popular-plan">
                                 <span>Popular
                                    plan</span>
                              </div>
                           </div>
                        </div>
                        <div className="col-xl-3 col-lg-6 col-md-6">
                           <div className="membership-box  mb-30">
                              <div className="membership-info">
                                 <h4>Basic</h4>
                                 <div className="membership-price">
                                    <span>19.00</span>
                                    <p>Free for starters</p>
                                 </div>
                                 <div className="membership-list">
                                    <ul>
                                       <li><i className="far fa-check"></i>Unlimited Devices</li>
                                       <li><i className="far fa-check"></i>Handle Basic Option</li>
                                       <li><i className="far fa-check"></i>Individual Course</li>
                                       <li><i className="far fa-check"></i>Instant Profile Option</li>
                                       <li><i className="far fa-check"></i>Full Time Access</li>
                                       <li><i className="far fa-check"></i>Anytime Backup</li>
                                    </ul>
                                 </div>
                              </div>
                              <Link href="/contact"><a className="membership-btn">Choose plan</a></Link>
                           </div>
                        </div>
                     </div>
                  </div>
                  <div className="tab-pane fade" id="yearly" role="tabpanel" aria-labelledby="yearly-tab">
                     <div className="row">
                        <div className="col-xl-3 col-lg-6 col-md-6">
                           <div className="membership-box mb-30">
                              <div className="membership-info">
                                 <h4>Basic</h4>
                                 <div className="membership-price">
                                    <span>Free</span>
                                    <p>Free for starters</p>
                                 </div>
                                 <div className="membership-list">
                                    <ul>
                                       <li><i className="far fa-check"></i>Up to 2 Devices</li>
                                       <li><i className="far fa-check"></i>Handle Basic Option</li>
                                       <li><i className="far fa-check"></i>Individual Course</li>
                                       <li><i className="far fa-check"></i>Free for starters</li>
                                    </ul>
                                 </div>
                              </div>
                              <Link href="/contact"><a className="membership-btn">Choose plan</a></Link>
                           </div>
                        </div>
                        <div className="col-xl-3 col-lg-6 col-md-6">
                           <div className="membership-box  mb-30">
                              <div className="membership-info">
                                 <h4>Basic</h4>
                                 <div className="membership-price">
                                    <span>19.00</span>
                                    <p>Free for starters</p>
                                 </div>
                                 <div className="membership-list">
                                    <ul>
                                       <li><i className="far fa-check"></i>Up to 5 Devices</li>
                                       <li><i className="far fa-check"></i>Handle Basic Option</li>
                                       <li><i className="far fa-check"></i>Individual Course</li>
                                       <li><i className="far fa-check"></i>3 Times Access</li>
                                    </ul>
                                 </div>
                              </div>
                              <Link href="/contact"><a className="membership-btn">Choose plan</a></Link>
                           </div>
                        </div>
                        <div className="col-xl-3 col-lg-6 col-md-6">
                           <div className="membership-box mb-30">
                              <div className="membership-info">
                                 <h4>Basic</h4>
                                 <div className="membership-price">
                                    <span>29.00</span>
                                    <p>Free for starters</p>
                                 </div>
                                 <div className="membership-list">
                                    <ul>
                                       <li><i className="far fa-check"></i>Up to 10 Devices</li>
                                       <li><i className="far fa-check"></i>Handle Basic Option</li>
                                       <li><i className="far fa-check"></i>Individual Course</li>
                                       <li><i className="far fa-check"></i>Full Time Access</li>
                                       <li><i className="far fa-check"></i>Monthly Backup</li>
                                    </ul>
                                 </div>
                              </div>
                              <Link href="/contact"><a className="membership-btn">Choose plan</a></Link>
                              <div className="Popular-plan">
                                 <span>Popular
                                    plan</span>
                              </div>
                           </div>
                        </div>
                        <div className="col-xl-3 col-lg-6 col-md-6">
                           <div className="membership-box  mb-30">
                              <div className="membership-info">
                                 <h4>Basic</h4>
                                 <div className="membership-price">
                                    <span>69.00</span>
                                    <p>Free for starters</p>
                                 </div>
                                 <div className="membership-list">
                                    <ul>
                                       <li><i className="far fa-check"></i>Unlimited Devices</li>
                                       <li><i className="far fa-check"></i>Handle Basic Option</li>
                                       <li><i className="far fa-check"></i>Individual Course</li>
                                       <li><i className="far fa-check"></i>Instant Profile Option</li>
                                       <li><i className="far fa-check"></i>Full Time Access</li>
                                       <li><i className="far fa-check"></i>Anytime Backup</li>
                                    </ul>
                                 </div>
                              </div>
                              <Link href="/contact"><a className="membership-btn">Choose plan</a></Link>
                           </div>
                        </div>
                     </div>
                  </div>
               </div>
            </div>
         </div>
      </section>
    );
};

export default MembershipPrice;