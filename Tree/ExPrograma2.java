package Tree;

import Generator.Converter;

public class ExPrograma2
{

    public static void main(String[] args)
    {
        /*MODULE PROGRAMA2*/
        Node<String> root = new Node<String>("MODULE");
        {
            Node<String> child = new Node<String>("programa2");
            Node<String> child0 = new Node<String>("DECLARATIONS");
            Node<String> child1 = new Node<String>("FUNCTIONS");
            {
                Node<String> child10 = new Node<String>("function");
                {
                    Node<String> child100 = new Node<String>("ASSIGN");
                    {
                        Node<String> child1000 = new Node<String>("ARRAY");
                        {
                            Node<String> child10000 = new Node<String>("a");
                            
                            child1000.addChild(child10000);
                        }
                        Node<String> child1001 = new Node<String>("f1");
                        
                        child100.addChild(child1000);
                        child100.addChild(child1001);
                    }
                    Node<String> child101 = new Node<String>("PARAMETERS");
                    {
                        Node<String> child1010 = new Node<String>("ARRAY");
                        {
                            Node<String> child10100 = new Node<String>("b");
                            
                            child1010.addChild(child10100);
                        }
                        
                        child101.addChild(child1010);
                    }
                    Node<String> child102 = new Node<String>("BLOCK");
                    {
                        Node<String> child1020 = new Node<String>("ASSIGN");
                        {
                            Node<String> child10200 = new Node<String>("i");
                            Node<String> child10201 = new Node<String>("0");
                            
                            child1020.addChild(child10200);
                            child1020.addChild(child10201);
                        }
                        Node<String> child1021 = new Node<String>("ASSIGN");
                        {
                            Node<String> child10210 = new Node<String>("N");
                            Node<String> child10211 = new Node<String>("CALL");
                            {
                                Node<String> child102110 = new Node<String>("b");
                                Node<String> child102111 = new Node<String>("size");
                                Node<String> child102112 = new Node<String>("PARAMETERS");
                                Node<String> child102113 = new Node<String>("INTEGER");
                                
                                child10211.addChild(child102110);
                                child10211.addChild(child102111);
                                child10211.addChild(child102112);
                                child10211.addChild(child102113);
                            }
                            
                            child1021.addChild(child10210);
                            child1021.addChild(child10211);
                        }
                        Node<String> child1022 = new Node<String>("ASSIGN");
                        {
                            Node<String> child10220 = new Node<String>("ARRAY");
                            {
                                Node<String> child102200 = new Node<String>("a");
                                
                                child10220.addChild(child102200);
                            }
                            Node<String> child10221 = new Node<String>("SIZE");
                            {
                                Node<String> child102210 = new Node<String>("N");
                                
                                child10221.addChild(child102210);
                            }
                            
                            child1022.addChild(child10220);
                            child1022.addChild(child10221);
                        }
                        Node<String> child1023 = new Node<String>("WHILE");
                        {
                            Node<String> child10230 = new Node<String>("<");
                            {
                                Node<String> child102300 = new Node<String>("i");
                                Node<String> child102301 = new Node<String>("CALL");
                                {
                                    Node<String> child1023010 = new Node<String>("b");
                                    Node<String> child1023011 = new Node<String>("size");
                                    Node<String> child1023012 = new Node<String>("PARAMETERS");
                                    Node<String> child1023013 = new Node<String>("INTEGER");

                                    child102301.addChild(child1023010);
                                    child102301.addChild(child1023011);
                                    child102301.addChild(child1023012);
                                    child102301.addChild(child1023013);
                                }                                
                                child10230.addChild(child102300);
                                child10230.addChild(child102301);
                            }
                            Node<String> child10231 = new Node<String>("BLOCK");
                            {
                                Node<String> child102310 = new Node<String>("ASSIGN");
                                {
                                    Node<String> child1023100 = new Node<String>("ARRAY");
                                    {
                                        Node<String> child10231000 = new Node<String>("a");
                                        Node<String> child10231001 = new Node<String>("i");
                                        
                                        child1023100.addChild(child10231000);
                                        child1023100.addChild(child10231001);
                                    }
                                    Node<String> child1023101 = new Node<String>("ARRAY");
                                    {
                                        Node<String> child10231010 = new Node<String>("b");
                                        Node<String> child10231011 = new Node<String>("i");
                                        
                                        child1023101.addChild(child10231010);
                                        child1023101.addChild(child10231011);
                                    }
                                    
                                    child102310.addChild(child1023100);
                                    child102310.addChild(child1023101);
                                }
                                Node<String> child102311 = new Node<String>("ASSIGN");
                                {
                                    Node<String> child1023110 = new Node<String>("i");
                                    Node<String> child1023111 = new Node<String>("ADD");
                                    {
                                        Node<String> child10231110 = new Node<String>("i");
                                        Node<String> child10231111 = new Node<String>("1");
                                        
                                        child1023111.addChild(child10231110);
                                        child1023111.addChild(child10231111);
                                    }
                                    
                                    child102311.addChild(child1023110);
                                    child102311.addChild(child1023111);
                                }
                                
                                child10231.addChild(child102310);
                                child10231.addChild(child102311);
                            }
                            
                            child1023.addChild(child10230);
                            child1023.addChild(child10231);
                        }
                        
                        child102.addChild(child1020);
                        child102.addChild(child1021);
                        child102.addChild(child1022);
                        child102.addChild(child1023);
                    }
                    
                    child10.addChild(child100);
                    child10.addChild(child101);
                    child10.addChild(child102);
                }
                Node<String> child11 = new Node<String>("function");
                {
                    Node<String> child110 = new Node<String>("ASSIGN");
                    {
                        Node<String> child1100 = new Node<String>("ARRAY");
                        {
                            Node<String> child11000 = new Node<String>("a");
                            
                            child1100.addChild(child11000);
                        }
                        Node<String> child1101 = new Node<String>("f2");
                        
                        child110.addChild(child1100);
                        child110.addChild(child1101);
                    }
                    Node<String> child111 = new Node<String>("PARAMETERS");
                    {
                        Node<String> child1110 = new Node<String>("N");
                        
                        child111.addChild(child1110);
                    }
                    Node<String> child112 = new Node<String>("BLOCK");
                    {
                        Node<String> child1120 = new Node<String>("ASSIGN");
                        {
                            Node<String> child11200 = new Node<String>("ARRAY");
                            {
                                Node<String> child112000 = new Node<String>("a");
                                
                                child11200.addChild(child112000);
                            }
                            Node<String> child11201 = new Node<String>("SIZE");
                            {
                                Node<String> child112010 = new Node<String>("N");
                                
                                child11201.addChild(child112010);
                            }
                            
                            child1120.addChild(child11200);
                            child1120.addChild(child11201);
                        }
                        
                        //Node<String> child1121 = new Node<String>("ASSIGN");
                        //{
                        //    Node<String> child11210 = new Node<String>("ARRAY");
                        //    {
                        //        Node<String> child112100 = new Node<String>("a");
                        //        
                        //        child11210.addChild(child112100);
                        //    }
                        //    Node<String> child11211 = new Node<String>("1");
                            
                        //    child1121.addChild(child11210);
                        //    child1121.addChild(child11211);
                        //}
                        
                        child112.addChild(child1120);
                        //child112.addChild(child1121);
                    }
                    
                    child11.addChild(child110);
                    child11.addChild(child111);
                    child11.addChild(child112);
                }
                Node<String> child12 = new Node<String>("function");
                {
                    Node<String> child120 = new Node<String>("main");
                    Node<String> child121 = new Node<String>("BLOCK");
                    {
                        Node<String> child1210 = new Node<String>("ASSIGN");
                        {
                            Node<String> child12100 = new Node<String>("ARRAY");
                            {
                                Node<String> child121000 = new Node<String>("b");
                                
                                child12100.addChild(child121000);
                            }
                            Node<String> child12101 = new Node<String>("SIZE");
                            {
                                Node<String> child121010 = new Node<String>("100");
                                
                                child12101.addChild(child121010);
                            }
                            
                            child1210.addChild(child12100);
                            child1210.addChild(child12101);
                        }
                        Node<String> child1211 = new Node<String>("ASSIGN");
                        {
                            Node<String> child12110 = new Node<String>("ARRAY");
                            {
                                Node<String> child121100 = new Node<String>("b");
                                Node<String> child121101 = new Node<String>("0");
                                
                                child12110.addChild(child121100);
                                child12110.addChild(child121101);
                            }
                            Node<String> child12111 = new Node<String>("1");
                            
                            child1211.addChild(child12110);
                            child1211.addChild(child12111);
                        }
                        Node<String> child1212 = new Node<String>("ASSIGN");
                        {
                            Node<String> child12120 = new Node<String>("ARRAY");
                            {
                                Node<String> child121200 = new Node<String>("b");
                                Node<String> child121201 = new Node<String>("99");
                                
                                child12120.addChild(child121200);
                                child12120.addChild(child121201);
                            }
                            Node<String> child12121 = new Node<String>("2");
                            
                            child1212.addChild(child12120);
                            child1212.addChild(child12121);
                        }
                        Node<String> child1213 = new Node<String>("ASSIGN");
                        {
                            Node<String> child12130 = new Node<String>("ARRAY");
                            {
                                Node<String> child121300 = new Node<String>("a");
                                
                                child12130.addChild(child121300);
                            }
                            Node<String> child12131 = new Node<String>("CALL");
                            {
                                Node<String> child121310 = new Node<String>("programa2");
                                Node<String> child121311 = new Node<String>("f1");
                                Node<String> child121312 = new Node<String>("PARAMETERS");
                                {
                                    Node<String> child1213120 = new Node<String>("ARRAY");
                                    {
                                        Node<String> child12131200 = new Node<String>("b");
                                        
                                        child1213120.addChild(child12131200);
                                    }
                                    
                                    child121312.addChild(child1213120);
                                }
                                Node<String> child121313 = new Node<String>("ARRAY");
                                
                                child12131.addChild(child121310);
                                child12131.addChild(child121311);
                                child12131.addChild(child121312);
                                child12131.addChild(child121313);
                            }
                            
                            child1213.addChild(child12130);
                            child1213.addChild(child12131);
                        }
                        Node<String> child1214 = new Node<String>("ASSIGN");
                        {
                            Node<String> child12140 = new Node<String>("first");
                            Node<String> child12141 = new Node<String>("ARRAY");
                            {
                                Node<String> child121410 = new Node<String>("a");
                                Node<String> child121411 = new Node<String>("0");
                                
                                child12141.addChild(child121410);
                                child12141.addChild(child121411);
                            }
                            
                            child1214.addChild(child12140);
                            child1214.addChild(child12141);
                        }
                        Node<String> child1215 = new Node<String>("ASSIGN");
                        {
                            Node<String> child12150 = new Node<String>("last");
                            Node<String> child12151 = new Node<String>("ARRAY");
                            {
                                Node<String> child121510 = new Node<String>("a");
                                Node<String> child121511 = new Node<String>("99");
                                
                                child12151.addChild(child121510);
                                child12151.addChild(child121511);
                            }
                            
                            child1215.addChild(child12150);
                            child1215.addChild(child12151);
                        }
                        Node<String> child1216 = new Node<String>("CALL");
                        {
                            Node<String> child12160 = new Node<String>("io");
                            Node<String> child12161 = new Node<String>("println");
                            Node<String> child12162 = new Node<String>("PARAMETERS");
                            {
                                Node<String> child121620 = new Node<String>("LDC");
                                {
                                    Node<String> child1216200 = new Node<String>("\"first: \"");
                                    
                                    child121620.addChild(child1216200);
                                }
                                Node<String> child121621 = new Node<String>("first");
                                
                                child12162.addChild(child121620);
                                child12162.addChild(child121621);
                            }
                            Node<String> child12163 = new Node<String>("void");
                            
                            child1216.addChild(child12160);
                            child1216.addChild(child12161);
                            child1216.addChild(child12162);
                            child1216.addChild(child12163);
                        }
                        Node<String> child1217 = new Node<String>("CALL");
                        {
                            Node<String> child12170 = new Node<String>("io");
                            Node<String> child12171 = new Node<String>("println");
                            Node<String> child12172 = new Node<String>("PARAMETERS");
                            {
                                Node<String> child121720 = new Node<String>("LDC");
                                {
                                    Node<String> child1217200 = new Node<String>("\"last: \"");
                                    
                                    child121720.addChild(child1217200);
                                }
                                Node<String> child121721 = new Node<String>("last");
                                
                                child12172.addChild(child121720);
                                child12172.addChild(child121721);
                            }
                            Node<String> child12173 = new Node<String>("void");
                            
                            child1217.addChild(child12170);
                            child1217.addChild(child12171);
                            child1217.addChild(child12172);
                            child1217.addChild(child12173);
                        }
                        Node<String> child1218 = new Node<String>("ASSIGN");
                        {
                            Node<String> child12180 = new Node<String>("ARRAY");
                            {
                                Node<String> child121800 = new Node<String>("a");
                                
                                child12180.addChild(child121800);
                            }
                            Node<String> child12181 = new Node<String>("CALL");
                            {
                                Node<String> child121810 = new Node<String>("programa2");
                                Node<String> child121811 = new Node<String>("f2");
                                Node<String> child121812 = new Node<String>("PARAMETERS");
                                {
                                    Node<String> child1218120 = new Node<String>("100");
                                    
                                    child121812.addChild(child1218120);
                                }
                                Node<String> child121813 = new Node<String>("ARRAY");
                                
                                child12181.addChild(child121810);
                                child12181.addChild(child121811);
                                child12181.addChild(child121812);
                                child12181.addChild(child121813);
                            }
                            
                            child1218.addChild(child12180);
                            child1218.addChild(child12181);
                        }
                        Node<String> child1219 = new Node<String>("ASSIGN");
                        {
                            Node<String> child12190 = new Node<String>("first");
                            Node<String> child12191 = new Node<String>("ARRAY");
                            {
                                Node<String> child121910 = new Node<String>("a");
                                Node<String> child121911 = new Node<String>("0");
                                
                                child12191.addChild(child121910);
                                child12191.addChild(child121911);
                            }
                            
                            child1219.addChild(child12190);
                            child1219.addChild(child12191);
                        }
                        Node<String> child121a = new Node<String>("ASSIGN");
                        {
                            Node<String> child121a0 = new Node<String>("last");
                            Node<String> child121a1 = new Node<String>("ARRAY");
                            {
                                Node<String> child121a10 = new Node<String>("a");
                                Node<String> child121a11 = new Node<String>("99");
                                
                                child121a1.addChild(child121a10);
                                child121a1.addChild(child121a11);
                            }
                            
                            child121a.addChild(child121a0);
                            child121a.addChild(child121a1);
                        }
                        Node<String> child121b = new Node<String>("CALL");
                        {
                            Node<String> child121b0 = new Node<String>("io");
                            Node<String> child121b1 = new Node<String>("println");
                            Node<String> child121b2 = new Node<String>("PARAMETERS");
                            {
                                Node<String> child121b20 = new Node<String>("LDC");
                                {
                                    Node<String> child121b200 = new Node<String>("\"first: \"");
                                    
                                    child121b20.addChild(child121b200);
                                }
                                Node<String> child121b21 = new Node<String>("first");
                                
                                child121b2.addChild(child121b20);
                                child121b2.addChild(child121b21);
                            }
                            Node<String> child121b3 = new Node<String>("void");
                            
                            child121b.addChild(child121b0);
                            child121b.addChild(child121b1);
                            child121b.addChild(child121b2);
                            child121b.addChild(child121b3);
                        }
                        Node<String> child121c = new Node<String>("CALL");
                        {
                            Node<String> child121c0 = new Node<String>("io");
                            Node<String> child121c1 = new Node<String>("println");
                            Node<String> child121c2 = new Node<String>("PARAMETERS");
                            {
                                Node<String> child121c20 = new Node<String>("LDC");
                                {
                                    Node<String> child121c200 = new Node<String>("\"last: \"");
                                    
                                    child121c20.addChild(child121c200);
                                }
                                Node<String> child121c21 = new Node<String>("last");
                                
                                child121c2.addChild(child121c20);
                                child121c2.addChild(child121c21);
                            }
                            Node<String> child121c3 = new Node<String>("void");
                            
                            child121c.addChild(child121c0);
                            child121c.addChild(child121c1);
                            child121c.addChild(child121c2);
                            child121c.addChild(child121c3);
                        }
                        
                        child121.addChild(child1210);
                        child121.addChild(child1211);
                        child121.addChild(child1212);
                        child121.addChild(child1213);
                        child121.addChild(child1214);
                        child121.addChild(child1215);
                        child121.addChild(child1216);
                        child121.addChild(child1217);
                        child121.addChild(child1218);
                        child121.addChild(child1219);
                        child121.addChild(child121a);
                        child121.addChild(child121b);
                        child121.addChild(child121c);
                    }
                    
                    child12.addChild(child120);
                    child12.addChild(child121);
                }
                
                child1.addChild(child10);
                child1.addChild(child11);
                child1.addChild(child12);
            }
            
            root.addChild(child);
            root.addChild(child0);
            root.addChild(child1);
        }
        
        Converter cv = new Converter("programa2.j");
        cv.Convert(root);
    }
}
