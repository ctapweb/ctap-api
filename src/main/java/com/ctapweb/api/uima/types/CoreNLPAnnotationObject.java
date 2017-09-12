

/* First created by JCasGen Wed Aug 30 10:21:03 CEST 2017 */
package com.ctapweb.api.uima.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.TOP;
import org.apache.uima.jcas.cas.ByteArray;


/** for storing a coreNLP annotation object in byte array

 * Updated by JCasGen Wed Aug 30 11:28:12 CEST 2017
 * XML source: /home/xiaobin/work/project/CTAP/ctap-api/src/main/resources/descriptors/type_systems/linguistic_types.xml
 * @generated */
public class CoreNLPAnnotationObject extends TOP {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(CoreNLPAnnotationObject.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated
   * @return index of the type  
   */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected CoreNLPAnnotationObject() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public CoreNLPAnnotationObject(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public CoreNLPAnnotationObject(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** 
   * <!-- begin-user-doc -->
   * Write your own initialization here
   * <!-- end-user-doc -->
   *
   * @generated modifiable 
   */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: objectBytes

  /** getter for objectBytes - gets 
   * @generated
   * @return value of the feature 
   */
  public ByteArray getObjectBytes() {
    if (CoreNLPAnnotationObject_Type.featOkTst && ((CoreNLPAnnotationObject_Type)jcasType).casFeat_objectBytes == null)
      jcasType.jcas.throwFeatMissing("objectBytes", "com.ctapweb.api.uima.types.CoreNLPAnnotationObject");
    return (ByteArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((CoreNLPAnnotationObject_Type)jcasType).casFeatCode_objectBytes)));}
    
  /** setter for objectBytes - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setObjectBytes(ByteArray v) {
    if (CoreNLPAnnotationObject_Type.featOkTst && ((CoreNLPAnnotationObject_Type)jcasType).casFeat_objectBytes == null)
      jcasType.jcas.throwFeatMissing("objectBytes", "com.ctapweb.api.uima.types.CoreNLPAnnotationObject");
    jcasType.ll_cas.ll_setRefValue(addr, ((CoreNLPAnnotationObject_Type)jcasType).casFeatCode_objectBytes, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for objectBytes - gets an indexed value - 
   * @generated
   * @param i index in the array to get
   * @return value of the element at index i 
   */
  public byte getObjectBytes(int i) {
    if (CoreNLPAnnotationObject_Type.featOkTst && ((CoreNLPAnnotationObject_Type)jcasType).casFeat_objectBytes == null)
      jcasType.jcas.throwFeatMissing("objectBytes", "com.ctapweb.api.uima.types.CoreNLPAnnotationObject");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((CoreNLPAnnotationObject_Type)jcasType).casFeatCode_objectBytes), i);
    return jcasType.ll_cas.ll_getByteArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((CoreNLPAnnotationObject_Type)jcasType).casFeatCode_objectBytes), i);}

  /** indexed setter for objectBytes - sets an indexed value - 
   * @generated
   * @param i index in the array to set
   * @param v value to set into the array 
   */
  public void setObjectBytes(int i, byte v) { 
    if (CoreNLPAnnotationObject_Type.featOkTst && ((CoreNLPAnnotationObject_Type)jcasType).casFeat_objectBytes == null)
      jcasType.jcas.throwFeatMissing("objectBytes", "com.ctapweb.api.uima.types.CoreNLPAnnotationObject");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((CoreNLPAnnotationObject_Type)jcasType).casFeatCode_objectBytes), i);
    jcasType.ll_cas.ll_setByteArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((CoreNLPAnnotationObject_Type)jcasType).casFeatCode_objectBytes), i, v);}
  }

    