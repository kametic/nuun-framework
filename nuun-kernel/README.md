Nuun Kernel is a software kernel for framework creation. It brings a powerful plugin design plus 
a JSR 330 compliant injection mechanism backed by Guice.
Plugin benefits from kernel capabilities by sendings request to it :
  - kernel params request :
  - Classpath scans request :on class by name, annotation, meta annotation or more generally Predicate
  - Binding Request : 
  
Nuun Kernel provides an SPI in order to bring IOC from other vendors.

Nuun Kernel come with 2 plugins:
  - Configuration Plugin : a plugin that inject the good property based on @Property annotation.
  - Log plugin : a Log handler that inject the good Log implementation

Samples and Documentation are coming !
