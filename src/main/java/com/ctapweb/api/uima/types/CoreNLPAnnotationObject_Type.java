
/* First created by JCasGen Wed Aug 30 10:21:03 CEST 2017 */
package com.ctapweb.api.uima.types;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.cas.TOP_Type;

/** for storing a coreNLP annotation object in byte array

 * Updated by JCasGen Wed Aug 30 11:28:12 CEST 2017
 * @generated */
public class CoreNLPAnnotationObject_Type extends TOP_Type {
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = CoreNLPAnnotationObject.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("com.ctapweb.api.uima.types.CoreNLPAnnotationObject");
 
  /** @generated */
  final Feature casFeat_objectBytes;
  /** @generated */
  final int     casFeatCode_objectBytes;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getObjectBytes(int addr) {
        if (featOkTst && casFeat_objectBytes == null)
      jcas.throwFeatMissing("objectBytes", "com.ctapweb.api.uima.types.CoreNLPAnnotationObject");
    return ll_cas.ll_getRefValue(addr, casFeatCode_objectBytes);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setObjectBytes(int addr, int v) {
        if (featOkTst && casFeat_objectBytes == null)
      jcas.throwFeatMissing("objectBytes", "com.ctapweb.api.uima.types.CoreNLPAnnotationObject");
    ll_cas.ll_setRefValue(addr, casFeatCode_objectBytes, v);}
    
   /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @return value at index i in the array 
   */
  public byte getObjectBytes(int addr, int i) {
        if (featOkTst && casFeat_objectBytes == null)
      jcas.throwFeatMissing("objectBytes", "com.ctapweb.api.uima.types.CoreNLPAnnotationObject");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getByteArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_objectBytes), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_objectBytes), i);
  return ll_cas.ll_getByteArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_objectBytes), i);
  }
   
  /** @generated
   * @param addr low level Feature Structure reference
   * @param i index of item in the array
   * @param v value to set
   */ 
  public void setObjectBytes(int addr, int i, byte v) {
        if (featOkTst && casFeat_objectBytes == null)
      jcas.throwFeatMissing("objectBytes", "com.ctapweb.api.uima.types.CoreNLPAnnotationObject");
    if (lowLevelTypeChecks)
      ll_cas.ll_setByteArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_objectBytes), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_objectBytes), i);
    ll_cas.ll_setByteArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_objectBytes), i, v);
  }
 



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public CoreNLPAnnotationObject_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_objectBytes = jcas.getRequiredFeatureDE(casType, "objectBytes", "uima.cas.ByteArray", featOkTst);
    casFeatCode_objectBytes  = (null == casFeat_objectBytes) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_objectBytes).getCode();

  }
}



    